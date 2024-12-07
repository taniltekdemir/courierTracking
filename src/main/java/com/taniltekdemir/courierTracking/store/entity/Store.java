package com.taniltekdemir.courierTracking.store.entity;

import com.taniltekdemir.courierTracking.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
    private String name;
    private Double lat;
    private Double lng;
}
