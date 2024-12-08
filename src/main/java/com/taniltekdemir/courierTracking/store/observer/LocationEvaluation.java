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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class LocationEvaluation {

    private static final Double staticDistance = 100D;
    public static final Long minWaitingForNewEntry = 60L;
    @Autowired
    private StoreService storeService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StoreEntryEntityService storeEntryEntityService;
    @Autowired
    private StoreEntryRepository storeEntryRepository;



    @Transactional
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
            if((Boolean) activeTrip.get("Result")){  /**Daha önce başlatılmış active bir trip i var.*/
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
        } else {
                /**Bu bir mağaza girişi değil değerlendirmeye gerek yok.**/
            return;
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
            response.put("Result",true);
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
            response.put("StoreId",nearestStoreList.get(0).toString());
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
        List<Store> storeAll = storeService.getStores(); //  TODO :: iyileştirme lazım, store ların kare içine alıp sadece buna uyanları filtrelemek gibi

        return storeAll.stream()
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
}