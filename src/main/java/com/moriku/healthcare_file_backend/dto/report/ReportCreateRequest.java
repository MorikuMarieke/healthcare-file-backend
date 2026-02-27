package com.moriku.healthcare_file_backend.dto.report;

public class ReportCreateRequest {

    private Long carePlanId;
    private String title;
    private String text;

    public ReportCreateRequest() {
    }

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