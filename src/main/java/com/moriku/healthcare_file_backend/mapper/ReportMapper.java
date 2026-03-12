package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.report.ReportResponse;
import com.moriku.healthcare_file_backend.model.Report;

public final class ReportMapper {

    private ReportMapper() {
    }

    public static ReportResponse toResponse(Report report) {
        return new ReportResponse(
            report.getId(),
            report.getCarePlan().getId(),
            report.getAuthor().getId(),
            report.getTitle(),
            report.getText(),
            report.getCreatedAt(),
            report.getUpdatedAt()
        );
    }
}