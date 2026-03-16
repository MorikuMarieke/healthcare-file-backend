package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.report_photo.ReportPhotoResponse;
import com.moriku.healthcare_file_backend.model.ReportPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportPhotoService {

    ReportPhotoResponse uploadPhotoToReport(Long reportId, MultipartFile file);

    List<ReportPhotoResponse> getAllPhotosForReport(Long reportId);

    ReportPhoto getReportPhotoById(Long photoId);

    void deleteReportPhotoById(Long photoId);

    List<ReportPhotoResponse> getMyPhotosForReport(Long reportId);

    ReportPhoto getMyReportPhotoById(Long reportId, Long photoId);
}