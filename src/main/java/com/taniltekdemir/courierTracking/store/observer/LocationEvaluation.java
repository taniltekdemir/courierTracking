package com.taniltekdemir.courierTracking.store.observer;

import com.taniltekdemir.courierTracking.Utils.Utils;
import com.taniltekdemir.courierTracking.courier.dto.CourierLocationDto;
import com.taniltekdemir.courierTracking.courier.service.LocationService;
import com.taniltekdemir.courierTracking.store.entity.Store;
import com.taniltekdemir.courierTracking.store.entity.StoreEntry;
import com.taniltekdemir.courierTracking.store.enums.TripStatus;
import com.taniltekdemir.courierTracking.store.repository.StoreEntryRepository;
import com.taniltekdemir.courierTracking.store.service.StoreEntryEntityService;
import com.taniltekdemir.courierTracking.store.service.StoreService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class LocationEvaluation {

    private static final Double staticDistance = 100D;
    public static final Long minWaitingForNewEntry = 60L;
    private static final double APPROX_LAT_DISTANCE = 0.001;
    private static final double APPROX_LNG_DISTANCE = 0.001;
    @Autowired
    private StoreService storeService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StoreEntryEntityService storeEntryEntityService;
    @Autowired
    private StoreEntryRepository storeEntryRepository;

    private Map<String, List<Store>> storeGrid = new HashMap<>();
    @PostConstruct
    private void initializeStoreGrid() {
        List<Store> stores = storeService.getStores();
        stores.forEach(store -> {
            String key = getGridKey(store.getLat(), store.getLng());
            storeGrid.computeIfAbsent(key, k -> new ArrayList<>()).add(store);
        });
    }


    @Transactional
    @EventListener
    public void evaluater(CourierLocationDto dto) {
/**
 *  lokasyon datasını kaydet
 *
 *  lokasyon bir mağaza girişi ise aktif teslimatı varmı bakılır.
 *  aktif teslimatı varsa kapatılır.
 *  mesafe hesaplanır.
 *
 *  mağaza girişi fakat aktif teslimatı yoksa acvtive teslimat başlatılır.
 *
 *  son mağaza girişi ve şimdikiede mağaza girişi ise zaman farkına bakılır 1 dk danz azsa mağaza girişi yazılmaz.
 *
 */

        Map<String,String> responseForStoreEnty = IsExistNearStore(dto);
        if(Boolean.parseBoolean(responseForStoreEnty.get("Result"))){  /**Bu bir mağaza girişi*/
            var activeTrip = IsExistActiveTrip(dto);
            if(Boolean.parseBoolean((String) activeTrip.get("Result"))){  /**Daha önce başlatılmış active bir trip i var.*/
                StoreEntry activeTripEntry =(StoreEntry) activeTrip.get("lastActiveTrip");
                if (entryTimeNotEnough(dto.getTimeStamp(),activeTripEntry)) {
                    return;
                }
                closeActiveTrip(dto,activeTripEntry.getId());
                calculateLastTrip(dto,activeTripEntry.getId());
                createStoreEntry(Long.parseLong(responseForStoreEnty.get("StoreId")), dto); /** Create new trip*/
            }else {
                createStoreEntry(Long.parseLong(responseForStoreEnty.get("StoreId")), dto);  /**Create active new trip*/
            }
        }
    }

    private void calculateLastTrip(CourierLocationDto dto, long activeStoreEntryID) {
        StoreEntry storeEntry = storeEntryRepository.findTopByCourierIdAndId(dto.getCourierId(), activeStoreEntryID);
        LocalDateTime startTripTime = storeEntry.getTimestamp();
        LocalDateTime endTripTime = dto.getTimeStamp();

        Double totalTripDistance = locationService.calculateTripDistance(dto.getCourierId(), startTripTime,endTripTime);
        storeEntry.setTripDistance(totalTripDistance);
        storeEntryRepository.save(storeEntry);
    }

    private void closeActiveTrip(CourierLocationDto dto, Long activeStoreEntryID) {
        StoreEntry storeEntry = storeEntryRepository.findTopByCourierIdAndId(dto.getCourierId(), activeStoreEntryID);
        storeEntry.setTripStatus(TripStatus.COMPLETED);
        storeEntryRepository.save(storeEntry);
    }


    private Map<String,Object> IsExistActiveTrip(CourierLocationDto dto) {
        Map<String,Object> response = new HashMap<>();
        StoreEntry lastStoreEntry = storeEntryRepository.findTopByCourierIdOrderByTimestampDesc(dto.getCourierId());
        if (lastStoreEntry != null && lastStoreEntry.getTripStatus() == TripStatus.ACTIVE) {
            response.put("Result","true");
            response.put("lastActiveTrip",lastStoreEntry);
        } else {
            response.put("Result","false");
        }
        return response;
    }

    private Map<String,String> IsExistNearStore(CourierLocationDto dto) {  /**mağaza girişi mi */
        Map<String,String> response = new HashMap<>();
        var nearestStoreList = getClosestStoreToTheLocation(dto.getLongitude(), dto.getLatitude());
        if (nearestStoreList.size() > 0){
            response.put("StoreId",nearestStoreList.get(0).getId().toString());
            response.put("Result","true");
            return response;
        } else {
            response.put("Result","false");
            return response;
        }
    }

    private void createStoreEntry(Long storeId,CourierLocationDto courierLocationDto) {

        StoreEntry storeEntry = new StoreEntry();
        storeEntry.setStoreId(storeId);
        storeEntry.setTimestamp(courierLocationDto.getTimeStamp());
        storeEntry.setCourierId(courierLocationDto.getCourierId());
        storeEntry.setTripStatus(TripStatus.ACTIVE);

        storeEntryEntityService.save(storeEntry);
    }

    private boolean entryTimeNotEnough(LocalDateTime courierTimeStamp, StoreEntry lastStoreEnrty) {

        long secondsDiff = SECONDS.between(lastStoreEnrty.getTimestamp(), courierTimeStamp);

        secondsDiff = Math.abs(secondsDiff);

        return (secondsDiff <= minWaitingForNewEntry);  // aradaki zaman 1 dk küçükse true döner
    }

    private List<Store> getClosestStoreToTheLocation(Double lon, Double lat) {
        List<Store> nearbyStores = getStoresInRegion(lat, lon);

        return nearbyStores.stream()
                .filter(store ->
                        Math.abs(store.getLat() - lat) <= APPROX_LAT_DISTANCE &&
                        Math.abs(store.getLng() - lon) <= APPROX_LNG_DISTANCE
                )
                .filter(store -> {
                    double distance = Utils.calculateDistanceMeters(
                            lat,
                            store.getLat(),
                            lon,
                            store.getLng()
                    );
                    return distance <= staticDistance;
                })
                .collect(Collectors.toList());
    }

    private String getGridKey(double lat, double lng) {
        int latKey = (int) (lat / APPROX_LAT_DISTANCE);
        int lngKey = (int) (lng / APPROX_LNG_DISTANCE);
        return latKey + "_" + lngKey;
    }
    private List<Store> getStoresInRegion(Double lat, Double lon) {
        String key = getGridKey(lat, lon);
        return storeGrid.getOrDefault(key, Collections.emptyList());
    }
}