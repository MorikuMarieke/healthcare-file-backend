package com.moriku.healthcare_file_backend.dto.care_plan;

public class CarePlanUpdateRequest {

    private String notes;

    private String medicalHistory;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}