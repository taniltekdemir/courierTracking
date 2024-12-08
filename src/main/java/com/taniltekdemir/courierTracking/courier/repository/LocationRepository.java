package com.taniltekdemir.courierTracking.courier.repository;

import com.taniltekdemir.courierTracking.courier.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT l FROM Location l WHERE l.courier.id = :courierId " +
            "AND l.timestamp BETWEEN :startTimestamp AND :endTimestamp order by l.timestamp asc ")
    List<Location> findLocationsByCourierAndTimestampRange(
            @Param("courierId") Long courierId,
            @Param("startTimestamp") LocalDateTime startTimestamp,
            @Param("endTimestamp") LocalDateTime endTimestamp
    );
}
