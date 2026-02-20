package com.moriku.healthcare_file_backend.dto.care_plan;

import jakarta.validation.constraints.NotBlank;

public class CarePlanUpdateRequest {
    @NotBlank
    private String notes;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}