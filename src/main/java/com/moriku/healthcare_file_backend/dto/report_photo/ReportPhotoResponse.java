package com.moriku.healthcare_file_backend.dto.report_photo;

import java.time.LocalDateTime;

public class ReportPhotoResponse {

    private Long id;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private LocalDateTime uploadedAt;
    private Long reportId;

    public ReportPhotoResponse() {
    }

    public ReportPhotoResponse(Long id, String fileName, String contentType, Long fileSize, LocalDateTime uploadedAt, Long reportId) {
        this.id = id;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt;
        this.reportId = reportId;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
}