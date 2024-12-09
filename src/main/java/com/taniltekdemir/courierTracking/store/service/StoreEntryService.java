package com.taniltekdemir.courierTracking.store.service;

import com.taniltekdemir.courierTracking.courier.service.LocationService;
import com.taniltekdemir.courierTracking.store.entity.StoreEntry;
import com.taniltekdemir.courierTracking.store.enums.TripStatus;
import com.taniltekdemir.courierTracking.store.repository.StoreEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreEntryService {

    @Autowired
    StoreEntryRepository storeEntryRepository;

    public Double getTotalTripDistanceByCourierId(Long courierId, LocalDateTime startdate, LocalDateTime endDate) {
        var responseList = storeEntryRepository.findAllByTripStatusAndCourierIdAndTimestampBetween(TripStatus.ACTIVE, courierId, startdate, endDate);

        return responseList.stream()
                .map(StoreEntry::getTripDistance)
                .filter(Objects::nonNull).mapToDouble(Double::doubleValue).sum();
    }
}
