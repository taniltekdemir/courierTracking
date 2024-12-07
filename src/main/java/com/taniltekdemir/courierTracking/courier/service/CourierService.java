package com.taniltekdemir.courierTracking.courier.service;

import com.taniltekdemir.courierTracking.common.exception.ResourceNotFoundException;
import com.taniltekdemir.courierTracking.courier.dto.CourierDto;
import com.taniltekdemir.courierTracking.courier.dto.CourierSaveDto;
import com.taniltekdemir.courierTracking.courier.entity.Courier;
import com.taniltekdemir.courierTracking.courier.repository.CourierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierService {

    @Autowired
    private CourierEntityService courierEntityService;
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public CourierDto saveCourier(CourierSaveDto courierSaveDto) {
        Courier entity = modelMapper.map(courierSaveDto, Courier.class);
        courierEntityService.save(entity);
        log.info("The courier with license plate {} was recorded in the system.", courierSaveDto.getLicensePlate());
        return modelMapper.map(entity,CourierDto.class);
    }
    @Transactional
    public CourierDto updateCourier(CourierDto courierDto) {

        Courier exitstCourier = courierRepository.findFirstByIdAndIsDeleted(courierDto.getId(), false);
        if (exitstCourier == null) {
            String message = String.format("Curier with id {} not found", courierDto.getId().toString());
            throw new ResourceNotFoundException(message);
        }
        exitstCourier.setName(courierDto.getFirstName());
        exitstCourier.setSurname(courierDto.getLastName());
        exitstCourier.setLicensePlate(courierDto.getLicensePlate());
        courierEntityService.save(exitstCourier);
        log.info("The courier with license plate {} was updated in the system.", exitstCourier.getLicensePlate());
        return modelMapper.map(exitstCourier,CourierDto.class);
    }

    public CourierDto findByLicensePlate(String licensePlate) {
        var entity = courierRepository.findFirstByLicensePlateAndIsDeleted(licensePlate, false);
        return modelMapper.map(entity,CourierDto.class);
    }
}
