package com.taniltekdemir.courierTracking.courier.service;

import com.taniltekdemir.courierTracking.common.service.BaseEntityService;
import com.taniltekdemir.courierTracking.courier.entity.Courier;
import com.taniltekdemir.courierTracking.courier.entity.Location;
import com.taniltekdemir.courierTracking.courier.repository.CourierRepository;
import com.taniltekdemir.courierTracking.courier.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationEntityService extends BaseEntityService<Location, LocationRepository> {
    public LocationEntityService(LocationRepository repository) {
        super(repository);
    }
}
