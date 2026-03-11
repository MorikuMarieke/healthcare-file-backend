package com.moriku.healthcare_file_backend.dto.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReportUpdateRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 5000)
    private String text;

    public ReportUpdateRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}