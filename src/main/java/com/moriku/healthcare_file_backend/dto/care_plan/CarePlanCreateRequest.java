package com.moriku.healthcare_file_backend.dto.care_plan;

import jakarta.validation.constraints.NotNull;

public class CarePlanCreateRequest {

    @NotNull
    private Long clientProfileId;

    public CarePlanCreateRequest(Long clientProfileId) {
        this.clientProfileId = clientProfileId;
    }

    public Long getClientProfileId() {
        return clientProfileId;
    }

    public void setClientProfileId(Long clientProfileId) {
        this.clientProfileId = clientProfileId;
    }
}