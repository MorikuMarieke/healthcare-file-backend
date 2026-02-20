package com.moriku.healthcare_file_backend.dto.care_plan;

import java.time.Instant;

public class CarePlanResponse {
    private Long id;
    private Long clientProfileId;
    private Instant createdAt;
    private String notes;

    public CarePlanResponse(Long id, Long clientProfileId, Instant createdAt, String notes) {
        this.id = id;
        this.clientProfileId = clientProfileId;
        this.createdAt = createdAt;
        this.notes = notes;
    }

//    public CarePlanResponse() {}

    public Long getId() {
        return id;
    }

    public Long getClientProfileId() {
        return clientProfileId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getNotes() {
        return notes;
    }

}