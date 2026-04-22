package com.moriku.healthcare_file_backend.dto.care_plan;

public class CarePlanResponse {

    private Long id;
    private Long clientProfileId;
    private String notes;
    private String medicalHistory;

    public CarePlanResponse(Long id, Long clientProfileId, String notes, String medicalHistory) {
        this.id = id;
        this.clientProfileId = clientProfileId;
        this.notes = notes;
        this.medicalHistory = medicalHistory;
    }

    public Long getId() {
        return id;
    }

    public Long getClientProfileId() {
        return clientProfileId;
    }

    public String getNotes() {
        return notes;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }
}