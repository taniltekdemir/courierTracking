package com.taniltekdemir.courierTracking.courier.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourierLocationDto {
    private Long courierId;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timeStamp;
}
