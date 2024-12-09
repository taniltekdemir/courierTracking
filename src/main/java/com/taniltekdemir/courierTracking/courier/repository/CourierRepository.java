package com.taniltekdemir.courierTracking.courier.repository;

import com.taniltekdemir.courierTracking.courier.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {

    Courier findFirstByIdAndIsDeleted(Long id, boolean isDeleted);
    Courier findFirstByLicensePlateAndIsDeleted(String plate, boolean isDeleted);
    List<Courier> findAllByIsDeleted(boolean isDeleted);
}
