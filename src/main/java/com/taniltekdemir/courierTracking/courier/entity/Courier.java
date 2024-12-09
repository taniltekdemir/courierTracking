package com.taniltekdemir.courierTracking.courier.entity;

import com.taniltekdemir.courierTracking.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "courier", uniqueConstraints = {@UniqueConstraint(columnNames = {"license_plate"})})
public class Courier extends BaseEntity{
    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;
    @Column(name = "surname", nullable = false)
    @NotBlank
    private String surname;
    @Column(name = "license_plate", nullable = false)
    @NotBlank
    private String licensePlate;
    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
