package com.taniltekdemir.courierTracking.store.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taniltekdemir.courierTracking.store.entity.Store;
import com.taniltekdemir.courierTracking.store.repository.StoreRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
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
    @Value("classpath:static/stores.json")
    private Resource storeJsonResource;

    @PostConstruct
    private void initialize() {
        instance = this;
        try {
            if (storeRepository.count() == 0) {
                refreshStoresFromJson();
            } else {
                refreshStoresFromDB();
            }
            refreshStoresFromDB();
        } catch (Exception e) {
            throw new RuntimeException("Initialization failed", e);
        }
    }

    private void refreshStoresFromDB() {
        stores = storeRepository.findAll();
    }

    public void refreshStoresFromJson() {
            ObjectMapper objectMapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("static/stores.json");
            try (InputStream inputStream = resource.getInputStream()) {
                var jsonStores = objectMapper.readValue(inputStream, new TypeReference<List<Store>>() {
                });
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
        if (instance == null) {
            instance = new StoreService();
        }
        return instance;
    }

    public List<Store> getStores() {
        return stores;
    }
}
