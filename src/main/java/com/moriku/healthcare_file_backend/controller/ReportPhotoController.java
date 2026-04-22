package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.report_photo.ReportPhotoResponse;
import com.moriku.healthcare_file_backend.model.ReportPhoto;
import com.moriku.healthcare_file_backend.service.ReportPhotoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportPhotoController {

    private final ReportPhotoService reportPhotoService;

    public ReportPhotoController(ReportPhotoService reportPhotoService) {
        this.reportPhotoService = reportPhotoService;
    }

    @PostMapping("/{reportId}/photos")
    public ReportPhotoResponse uploadPhoto(
        @PathVariable Long reportId,
        @RequestParam("file") MultipartFile file
    ) {
        return reportPhotoService.uploadPhotoToReport(reportId, file);
    }

    @GetMapping("/{reportId}/photos")
    public List<ReportPhotoResponse> getPhotosByReport(
        @PathVariable Long reportId
    ) {
        return reportPhotoService.getAllPhotosForReport(reportId);
    }

    @GetMapping("/{reportId}/photos/{photoId}/content")
    public ResponseEntity<byte[]> getPhotoContent(
        @PathVariable Long reportId,
        @PathVariable Long photoId
    ) {
        ReportPhoto photo = reportPhotoService.getReportPhotoById(reportId, photoId);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(photo.getContentType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + photo.getFileName() + "\"")
            .body(photo.getData());
    }

    @DeleteMapping("/{reportId}/photos/{photoId}")
    public void deletePhoto(
        @PathVariable Long reportId,
        @PathVariable Long photoId
    ) {
        reportPhotoService.deleteReportPhotoById(reportId, photoId);
    }
}