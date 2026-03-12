package com.moriku.healthcare_file_backend.dto.report;

import java.time.LocalDateTime;

public class ReportResponse {

    private Long id;
    private Long carePlanId;
    private Long authorEmployeeProfileId;
    private String title;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReportResponse(Long id,
                          Long carePlanId,
                          Long authorEmployeeProfileId,
                          String title,
                          String text,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt) {
        this.id = id;
        this.carePlanId = carePlanId;
        this.authorEmployeeProfileId = authorEmployeeProfileId;
        this.title = title;
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getCarePlanId() {
        return carePlanId;
    }

    public Long getAuthorEmployeeProfileId() {
        return authorEmployeeProfileId;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}