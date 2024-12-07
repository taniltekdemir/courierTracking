package com.taniltekdemir.courierTracking.courier.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationDto {
    @NotNull
    @JsonProperty("courier")
    private String licensePlate;
    @NotNull
    @JsonProperty("lat")
    private Double latitude;
    @NotNull
    @JsonProperty("lng")
    private Double longitude;
    @NotNull
    @JsonProperty("time")
    private LocalDateTime timeStamp;
}
