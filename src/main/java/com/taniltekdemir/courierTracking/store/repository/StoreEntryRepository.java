package com.taniltekdemir.courierTracking.store.repository;

import com.taniltekdemir.courierTracking.store.entity.Store;
import com.taniltekdemir.courierTracking.store.entity.StoreEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreEntryRepository extends JpaRepository<StoreEntry, Long> {

    StoreEntry findTopByCourierIdOrderByTimestampDesc(Long courierId);

    StoreEntry findTopByCourierIdAndId(Long courierId, Long Id);
}
