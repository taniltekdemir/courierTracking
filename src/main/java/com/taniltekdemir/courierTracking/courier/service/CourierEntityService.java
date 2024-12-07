package com.taniltekdemir.courierTracking.courier.service;

import com.taniltekdemir.courierTracking.common.service.BaseEntityService;
import com.taniltekdemir.courierTracking.courier.entity.Courier;
import com.taniltekdemir.courierTracking.courier.repository.CourierRepository;
import org.springframework.stereotype.Service;

@Service
public class CourierEntityService extends BaseEntityService<Courier, CourierRepository> {
    public CourierEntityService(CourierRepository repository) {
        super(repository);
    }
}
