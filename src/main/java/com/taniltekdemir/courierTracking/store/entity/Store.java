package com.taniltekdemir.courierTracking.store.entity;

import com.taniltekdemir.courierTracking.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "store", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Store extends BaseEntity {

    @Column(name = "name", length = 150, nullable = false)
    @NotBlank
    private String name;
    @Column(name = "lat", nullable = false)
    @NotNull
    private Double lat;

    @Column(name = "lng", nullable = false)
    @NotNull
    private Double lng;
}
