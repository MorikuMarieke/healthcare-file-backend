package com.moriku.healthcare_file_backend.dto.report;

public class ReportUpdateRequest {

    private String title;
    private String text;

    public ReportUpdateRequest() {
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}