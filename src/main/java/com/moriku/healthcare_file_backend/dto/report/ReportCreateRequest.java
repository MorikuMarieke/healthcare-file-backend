package com.moriku.healthcare_file_backend.dto.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReportCreateRequest {

    @NotNull
    private Long carePlanId;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 5000)
    private String text;

    public Long getCarePlanId() {
        return carePlanId;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}