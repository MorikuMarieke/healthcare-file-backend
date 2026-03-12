package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.report_photo.ReportPhotoResponse;
import com.moriku.healthcare_file_backend.model.ReportPhoto;

import java.util.ArrayList;
import java.util.List;

public final class ReportPhotoMapper {

    private ReportPhotoMapper() {
    }

    public static ReportPhotoResponse toResponse(ReportPhoto reportPhoto) {
        return new ReportPhotoResponse(
            reportPhoto.getId(),
            reportPhoto.getFileName(),
            reportPhoto.getContentType(),
            reportPhoto.getFileSize(),
            reportPhoto.getUploadedAt(),
            reportPhoto.getReport().getId()
        );
    }

    public static List<ReportPhotoResponse> toResponseList(List<ReportPhoto> reportPhotos) {
        List<ReportPhotoResponse> responses = new ArrayList<>();

        for (ReportPhoto reportPhoto : reportPhotos) {
            responses.add(toResponse(reportPhoto));
        }

        return responses;
    }

}