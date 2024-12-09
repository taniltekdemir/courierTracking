package com.taniltekdemir.courierTracking.store.repository;

import com.taniltekdemir.courierTracking.store.entity.Store;
import com.taniltekdemir.courierTracking.store.entity.StoreEntry;
import com.taniltekdemir.courierTracking.store.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StoreEntryRepository extends JpaRepository<StoreEntry, Long> {

    StoreEntry findTopByCourierIdOrderByTimestampDesc(Long courierId);

    StoreEntry findTopByCourierIdAndId(Long courierId, Long Id);


    List<StoreEntry> findAllByTripStatusAndCourierIdAndTimestampBetween(TripStatus tripStatus, Long courierId, LocalDateTime startDate,LocalDateTime endDate);
}
