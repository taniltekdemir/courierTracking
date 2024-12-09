package com.taniltekdemir.courierTracking.courier.entity;

import com.taniltekdemir.courierTracking.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "location")
public class Location extends BaseEntity{

    @Column(name = "latitude", nullable = false)
    @NotNull
    private Double latitude;
    @Column(name = "longitude", nullable = false)
    @NotNull
    private Double longitude;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "courier_id", referencedColumnName = "id")
    private Courier courier;
}
