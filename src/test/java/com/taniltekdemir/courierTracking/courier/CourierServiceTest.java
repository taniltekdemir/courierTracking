package com.taniltekdemir.courierTracking.courier;

import com.taniltekdemir.courierTracking.courier.controller.CourierController;
import com.taniltekdemir.courierTracking.courier.dto.CourierDto;
import com.taniltekdemir.courierTracking.courier.dto.CourierSaveDto;
import com.taniltekdemir.courierTracking.courier.repository.CourierRepository;
import com.taniltekdemir.courierTracking.courier.service.CourierService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CourierServiceTest {
    @Mock
    private CourierService courierService;

    @InjectMocks
    private CourierController courierController;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CourierRepository courierRepository;

    @Test
    void testSaveCourier_Success() throws URISyntaxException {
        CourierSaveDto courierSaveDto = new CourierSaveDto();
        courierSaveDto.setName("anıl");
        courierSaveDto.setLicensePlate("1234567890");;

        CourierDto savedCourier = new CourierDto();
        savedCourier.setId(1L);
        savedCourier.setName("anıl");
        savedCourier.setLicensePlate("1234567890");

        when(courierService.saveCourier(courierSaveDto)).thenReturn(savedCourier);

        ResponseEntity<?> response = courierController.saveCourier(courierSaveDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(savedCourier);

        verify(courierService, times(1)).saveCourier(courierSaveDto);
    }

    @Test
    void testSaveCourier_Failure() throws URISyntaxException {
        CourierSaveDto courierSaveDto = new CourierSaveDto();
        courierSaveDto.setName("Invalid Courier");

        when(courierService.saveCourier(courierSaveDto)).thenThrow(new RuntimeException("Invalid data"));

        ResponseEntity<?> response = courierController.saveCourier(courierSaveDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("java.lang.RuntimeException: Invalid data");

        verify(courierService, times(1)).saveCourier(courierSaveDto);
    }


}
