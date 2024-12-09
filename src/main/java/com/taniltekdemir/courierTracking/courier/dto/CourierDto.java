package com.taniltekdemir.courierTracking.courier.dto;
import lombok.Data;

@Data
public class CourierDto {
    private Long id;
    private String name;
    private String surname;
    private  String licensePlate;
}
