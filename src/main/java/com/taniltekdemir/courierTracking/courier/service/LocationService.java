package com.taniltekdemir.courierTracking.courier.service;

import com.taniltekdemir.courierTracking.common.exception.ResourceNotFoundException;
import com.taniltekdemir.courierTracking.courier.dto.CourierDto;
import com.taniltekdemir.courierTracking.courier.dto.LocationDto;
import com.taniltekdemir.courierTracking.courier.entity.Location;
import com.taniltekdemir.courierTracking.courier.repository.CourierRepository;
import com.taniltekdemir.courierTracking.courier.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    @Autowired
    private CourierService courierService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationEntityService locationEntityService;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public void insertLocation(LocationDto locationDto) {
        CourierDto courierDto = courierService.findByLicensePlate(locationDto.getLicensePlate());

        if(courierDto == null) {
            String message = String.format("Curier with plate {} not found", locationDto.getLicensePlate());
            throw new ResourceNotFoundException(message);
        }
        Location locationEntity = modelMapper.map(locationDto, Location.class);

        locationEntityService.save(locationEntity);

    }
}
