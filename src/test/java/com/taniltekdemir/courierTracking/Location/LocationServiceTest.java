package com.taniltekdemir.courierTracking.Location;


import com.taniltekdemir.courierTracking.courier.dto.CourierLocationDto;
import com.taniltekdemir.courierTracking.courier.dto.LocationDto;
import com.taniltekdemir.courierTracking.courier.entity.Courier;
import com.taniltekdemir.courierTracking.courier.entity.Location;
import com.taniltekdemir.courierTracking.courier.service.CourierService;
import com.taniltekdemir.courierTracking.courier.service.LocationEntityService;
import com.taniltekdemir.courierTracking.courier.service.LocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class LocationServiceTest {
    @InjectMocks
    private LocationService locationService;

    @Mock
    private CourierService courierService;

    @Mock
    private LocationEntityService locationEntityService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    public void testInsertLocation_Success() {
        String licensePlate = "06DG7889";

        Courier courier = new Courier();
        courier.setId(1L);
        courier.setName("test");
        courier.setSurname("test");
        courier.setLicensePlate(licensePlate);

        when(courierService.findByLicensePlate(licensePlate)).thenReturn(courier);

        LocationDto locationDto = new LocationDto();
        locationDto.setLatitude(40.9923307);
        locationDto.setLongitude(29.1244229);
        locationDto.setTimeStamp(LocalDateTime.now());
        locationDto.setLicensePlate(licensePlate);

        locationService.insertLocation(locationDto);
        verify(locationEntityService, times(1)).save(any(Location.class));

        verify(applicationEventPublisher, times(1)).publishEvent(any(CourierLocationDto.class));
    }

    @Test
    public void testInsertLocation_CourierNotFound() {

        String licensePlate = "06XYZ123";

        LocationDto locationDto = new LocationDto();
        locationDto.setLatitude(40.9923307);
        locationDto.setLongitude(29.1244229);
        locationDto.setTimeStamp(LocalDateTime.now());
        locationDto.setLicensePlate(licensePlate);

        verify(locationEntityService, times(0)).save(any(Location.class));
        verify(applicationEventPublisher, times(0)).publishEvent(any(CourierLocationDto.class));
    }

}
