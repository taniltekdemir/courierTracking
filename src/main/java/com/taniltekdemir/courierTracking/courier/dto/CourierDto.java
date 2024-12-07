package com.taniltekdemir.courierTracking.courier.dto;
import lombok.Data;

@Data
public class CourierDto {
    private Long id;
    private String firstName;
    private String lastName;

    private  String licensePlate;
}
