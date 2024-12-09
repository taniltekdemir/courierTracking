package com.taniltekdemir.courierTracking.courier.service;

import com.taniltekdemir.courierTracking.Utils.Utils;
import com.taniltekdemir.courierTracking.common.exception.ResourceNotFoundException;
import com.taniltekdemir.courierTracking.courier.dto.CourierDto;
import com.taniltekdemir.courierTracking.courier.dto.CourierLocationDto;
import com.taniltekdemir.courierTracking.courier.dto.LocationDto;
import com.taniltekdemir.courierTracking.courier.entity.Courier;
import com.taniltekdemir.courierTracking.courier.entity.Location;
import com.taniltekdemir.courierTracking.courier.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    @Autowired
    private CourierService courierService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationEntityService locationEntityService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void insertLocation(LocationDto locationDto) {
        Courier courier = courierService.findByLicensePlate(locationDto.getLicensePlate());

        if(courier == null) {
            String message = String.format("Courier with plate {} not found", locationDto.getLicensePlate());
            throw new ResourceNotFoundException(message);
        }
        Location locationEntity = new Location();
        locationEntity.setLatitude(locationDto.getLatitude());
        locationEntity.setLongitude(locationDto.getLongitude());
        locationEntity.setTimestamp(locationDto.getTimeStamp());
        locationEntity.setCourier(courier);
        locationEntityService.save(locationEntity);

        CourierLocationDto courierLocationDto = new CourierLocationDto();
        courierLocationDto.setCourierId(courier.getId());
        courierLocationDto.setLongitude(locationDto.getLongitude());
        courierLocationDto.setLatitude(locationDto.getLatitude());
        courierLocationDto.setTimeStamp(locationDto.getTimeStamp());

        applicationEventPublisher.publishEvent(courierLocationDto);
    }
    public double calculateTripDistance(Long courierId, LocalDateTime startTripTime, LocalDateTime endTripTime) {

        List<Location> locationList =  locationRepository.findLocationsByCourierAndTimestampRange(courierId,startTripTime,endTripTime);
        double totalDistance = 0.0;
        for (int i = 0; i < locationList.size() -1; i++){

            Location location1 = locationList.get(i);
            Location location2 = locationList.get(i+1);

            Double latitude1 = location1.getLatitude();
            Double longitude1 = location1.getLongitude();

            Double latitude2 = location2.getLatitude();
            Double longitude2 = location2.getLongitude();
            double distance = Utils.calculateDistanceMeters(
                    latitude1,
                    latitude2,
                    longitude1,
                    longitude2
            );
            totalDistance = totalDistance + distance;
        }
        return totalDistance;
    }
}
