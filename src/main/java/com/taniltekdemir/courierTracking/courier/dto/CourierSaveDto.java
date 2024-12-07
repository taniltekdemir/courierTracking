package com.taniltekdemir.courierTracking.courier.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourierSaveDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private  String licensePlate;
}
