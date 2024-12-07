package com.taniltekdemir.courierTracking.courier.repository;

import com.taniltekdemir.courierTracking.courier.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
