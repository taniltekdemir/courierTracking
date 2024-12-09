package com.taniltekdemir.courierTracking.courier.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourierSaveDto {
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private  String licensePlate;
}
