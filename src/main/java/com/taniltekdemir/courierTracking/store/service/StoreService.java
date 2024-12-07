package com.taniltekdemir.courierTracking.store.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taniltekdemir.courierTracking.store.entity.Store;
import com.taniltekdemir.courierTracking.store.repository.StoreRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StoreService {

    private static StoreService instance;
    @Autowired
    private StoreRepository storeRepository;
    private List<Store> stores;
    private static final String storeJsonPath = "static/stores.json";

    @PostConstruct
    private void initialize() {
        instance = this;
        refreshStoresFromDB();
    }

    private void refreshStoresFromDB() {
        stores = storeRepository.findAll();
    }

    public void refreshStoresFromJson() {
        try{
            String storejsonContent = new String(Files.readAllBytes(Path.of(storeJsonPath)));
            ObjectMapper objectMapper = new ObjectMapper();
            List<Store> jsonStores = objectMapper.readValue(storejsonContent, new TypeReference<List<Store>>() {});

            List<Store> existStores = storeRepository.findAll();

            Map<String, Store> jsonStoreMap = jsonStores.stream()
                    .collect(Collectors.toMap(
                            Store::getName,
                            store -> store
                    ));
//            if exist, update
            existStores.forEach(store -> {
                String key = store.getName();
                if (jsonStoreMap.containsKey(key)) {
                    Store jsonStore = jsonStoreMap.get(key);
                    store.setLat(jsonStore.getLat());
                    store.setLng(jsonStore.getLng());
                    storeRepository.save(store);
                    jsonStoreMap.remove(key);
                }else {
                    // new added stores
                    storeRepository.delete(store);
                }
            });
//            if not exist, remove
            storeRepository.saveAll(jsonStoreMap.values());
            log.info("new stores.json file has been read and updated Db records");
            refreshStoresFromDB();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized StoreService getInstance() {
        return instance;
    }

    public List<Store> getStores() {
        return stores;
    }
}
