package com.taniltekdemir.courierTracking.store.service;

import com.taniltekdemir.courierTracking.common.service.BaseEntityService;
import com.taniltekdemir.courierTracking.store.entity.StoreEntry;
import com.taniltekdemir.courierTracking.store.repository.StoreEntryRepository;
import org.springframework.stereotype.Service;
@Service
public class StoreEntryEntityService extends BaseEntityService<StoreEntry, StoreEntryRepository> {
    public StoreEntryEntityService(StoreEntryRepository repository) {
        super(repository);
    }
}