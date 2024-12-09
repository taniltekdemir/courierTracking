package com.taniltekdemir.courierTracking.courier.controller;

import com.taniltekdemir.courierTracking.courier.dto.CourierDto;
import com.taniltekdemir.courierTracking.courier.dto.CourierSaveDto;
import com.taniltekdemir.courierTracking.courier.dto.LocationDto;
import com.taniltekdemir.courierTracking.courier.entity.Courier;
import com.taniltekdemir.courierTracking.courier.service.CourierService;
import com.taniltekdemir.courierTracking.courier.service.LocationService;
import com.taniltekdemir.courierTracking.store.entity.StoreEntry;
import com.taniltekdemir.courierTracking.store.service.StoreEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

@Tag(description = "/courier", name = "Courier API")
@RestController
@RequestMapping(path = "api/v1/courier")
public class CourierController {
    @Autowired
    CourierService courierService;
    @Autowired
    LocationService locationService;

    @Autowired
    StoreEntryService storeEntryService;
    @GetMapping
    public List<CourierDto> getCouriers() {
        return courierService.getCouriers();
    }
    @PostMapping
    public ResponseEntity<?> saveCourier(@RequestBody @Valid CourierSaveDto courierRequest) throws URISyntaxException {
        try {
            CourierDto newCourier = courierService.saveCourier(courierRequest);
            URI location = new URI("/courier/" + newCourier.getId());
            return ResponseEntity.created(location).body(newCourier);
        }catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.toString());
        }
    }
    @PutMapping
    public ResponseEntity<Void> updateCourier(@RequestBody @Valid CourierDto courierRequest) {
        courierService.updateCourier(courierRequest);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/location")
    public ResponseEntity<Void> insertCourierLocation(@Valid @RequestBody LocationDto locationRequest) {
        locationService.insertLocation(locationRequest);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/total-trip-distance/{courierId}")
    public ResponseEntity<Double> getTotalTripDistance(
            @PathVariable("courierId") Long courierId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate)  {
        storeEntryService.getTotalTripDistanceByCourierId(courierId, startDate, endDate);
        return ResponseEntity.ok().build();
    }
}
