package com.taniltekdemir.courierTracking.store.repository;

import com.taniltekdemir.courierTracking.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}
