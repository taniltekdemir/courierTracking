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
import java.util.Random;

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
        Double totalDistance = storeEntryService.getTotalTripDistanceByCourierId(courierId, startDate, endDate);
        return ResponseEntity.ok(totalDistance);
    }
    @PostMapping("/should_insert_100_location_together")
    public void should_insert_100_location_together() throws InterruptedException {
        CourierSaveDto newInsert = new CourierSaveDto();
        newInsert.setName("can");
        newInsert.setSurname("elitaş");
        newInsert.setLicensePlate("06DG7889");
        CourierDto newCourier = courierService.saveCourier(newInsert);

        double[][] storeLocations = { // Store 1
                {40.986106, 29.1161293}, // Store 2
                {41.0066851, 28.6552262}, // Store 3
                {41.055783, 29.0210292}, // Store 4
                {40.9632463, 29.0630908},// Store 5
                {40.9923307, 29.1244229},
        };

        Random random = new Random();

        for (int i = 1; i <= 20; i++) {
            LocationDto locationDto = new LocationDto();

            if (i % 5 == 0) {
                int storeIndex = (i / 5 - 1) % storeLocations.length;
                locationDto.setLatitude(storeLocations[storeIndex][0]);
                locationDto.setLongitude(storeLocations[storeIndex][1]);
                System.out.println("Store insert #" + i + ": " + locationDto);
            } else {
                double baseLat = 40.9923307;
                double baseLng = 29.1244229;
                if (i != 1) {
                    locationDto.setLatitude(baseLat + random.nextDouble() * 0.008 - 0.004);
                    locationDto.setLongitude(baseLng + random.nextDouble() * 0.008 - 0.004);
                } else {
                    locationDto.setLatitude(baseLat);
                    locationDto.setLongitude(baseLng);
                }
            }

            locationDto.setLicensePlate("06DG7889");
            locationDto.setTimeStamp(LocalDateTime.now());
            locationService.insertLocation(locationDto);
            System.out.println("Sending location #" + i + ": " + locationDto);

            Thread.sleep(61000);
        }
    }
}
