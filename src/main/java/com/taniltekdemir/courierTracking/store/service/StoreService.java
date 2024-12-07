package com.taniltekdemir.courierTracking.store.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taniltekdemir.courierTracking.store.entity.Store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class StoreService {

    private static StoreService instance;

    private List<Store> stores;

    private static final String storeJsonPath = "static/stores.json";

    private StoreService() {
        try{
            String storejsonContent = new String(Files.readAllBytes(Path.of(storeJsonPath)));
            ObjectMapper objectMapper = new ObjectMapper();
            this.stores = objectMapper.readValue(storejsonContent, new TypeReference<List<Store>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized StoreService getInstance() {
        if (instance == null) {
            instance = new StoreService();
        }
        return instance;
    }

    public List<Store> getStores() {
        return stores;
    }
}
