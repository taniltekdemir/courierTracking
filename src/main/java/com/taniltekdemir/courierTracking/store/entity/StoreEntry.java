package com.taniltekdemir.courierTracking.store.entity;

import com.taniltekdemir.courierTracking.common.entity.BaseEntity;
import com.taniltekdemir.courierTracking.store.enums.TripStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "store_entry")
public class StoreEntry extends BaseEntity {

    @Column(name = "courier_id", nullable = false)
    private Long courierId;

    @Column(name = "store_id", nullable = false)
    private Long storeId;
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    @Column(name = "trip_status", length = 30)
    private TripStatus tripStatus;
    @Column(name = "trip_distance", precision = 20, scale = 2)
    private Double tripDistance;
}
