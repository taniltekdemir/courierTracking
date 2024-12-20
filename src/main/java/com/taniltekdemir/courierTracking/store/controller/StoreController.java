package com.taniltekdemir.courierTracking.store.controller;

import com.taniltekdemir.courierTracking.store.entity.Store;
import com.taniltekdemir.courierTracking.store.service.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(description = "/stores", name = "Store API")
@RestController
@RequestMapping(path = "api/v1/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping("/refresh-json")
    public void refreshFromJson() {
        storeService.refreshStoresFromJson();
    }

    @GetMapping("/")
    public List<Store> getStores() {
        return storeService.getStores();
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Swagger!";
    }

}
