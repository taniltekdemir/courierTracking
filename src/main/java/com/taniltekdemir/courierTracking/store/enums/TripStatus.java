package com.taniltekdemir.courierTracking.store.enums;

public enum TripStatus {

    ACTIVE("Active"),
    COMPLETED("Completed");

    private String status;

    TripStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
