package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.report_photo.ReportPhotoResponse;
import com.moriku.healthcare_file_backend.mapper.ReportPhotoMapper;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.Report;
import com.moriku.healthcare_file_backend.model.ReportPhoto;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.ReportPhotoRepository;
import com.moriku.healthcare_file_backend.repository.ReportRepository;
import com.moriku.healthcare_file_backend.security.SecurityContextService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportPhotoServiceImpl implements ReportPhotoService {

    private static final int MAX_PHOTOS_PER_REPORT = 10;

    private final ReportPhotoRepository reportPhotoRepository;
    private final ReportRepository reportRepository;
    private final SecurityContextService securityContextService;

    public ReportPhotoServiceImpl(ReportPhotoRepository reportPhotoRepository, ReportRepository reportRepository, SecurityContextService securityContextService) {
        this.reportPhotoRepository = reportPhotoRepository;
        this.reportRepository = reportRepository;
        this.securityContextService = securityContextService;
    }

    @Override
    @Transactional
    public ReportPhotoResponse uploadPhotoToReport(Long reportId, MultipartFile file) {
        Report report = getReportByIdOrThrow(reportId);
        validateStaffAccessToReport(report);
        validateUploadFile(file);
        validatePhotoLimit(reportId);

        ReportPhoto reportPhoto = new ReportPhoto();
        reportPhoto.setFileName(file.getOriginalFilename());
        reportPhoto.setContentType(file.getContentType());
        reportPhoto.setFileSize(file.getSize());
        reportPhoto.setUploadedAt(LocalDateTime.now());
        reportPhoto.setReport(report);

        try {
            reportPhoto.setData(file.getBytes());
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not read uploaded file.");
        }

        ReportPhoto savedPhoto = reportPhotoRepository.save(reportPhoto);
        return ReportPhotoMapper.toResponse(savedPhoto);
    }

    @Override
    public List<ReportPhotoResponse> getAllPhotosForReport(Long reportId) {
        Report report = getReportByIdOrThrow(reportId);
        validateStaffAccessToReport(report);

        List<ReportPhoto> reportPhotos = reportPhotoRepository.findAllByReportIdOrderByUploadedAtAsc(reportId);
        return ReportPhotoMapper.toResponseList(reportPhotos);
    }

    @Override
    public ReportPhoto getReportPhotoById(Long photoId) {
        ReportPhoto reportPhoto = getReportPhotoByIdOrThrow(photoId);
        validateStaffAccessToReport(reportPhoto.getReport());
        return reportPhoto;
    }

    @Override
    @Transactional
    public void deleteReportPhotoById(Long photoId) {
        ReportPhoto reportPhoto = getReportPhotoByIdOrThrow(photoId);
        validateStaffAccessToReport(reportPhoto.getReport());

        reportPhotoRepository.delete(reportPhoto);
    }

    private Report getReportByIdOrThrow(Long reportId) {
        return reportRepository.findById(reportId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found."));
    }

    private ReportPhoto getReportPhotoByIdOrThrow(Long photoId) {
        return reportPhotoRepository.findById(photoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report photo not found."));
    }

    private void validateUploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required.");
        }

        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only image files are allowed.");
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File name is required.");
        }
    }

    private void validatePhotoLimit(Long reportId) {
        long photoCount = reportPhotoRepository.countByReportId(reportId);

        if (photoCount >= MAX_PHOTOS_PER_REPORT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A report can contain a maximum of 10 photos.");
        }
    }

    private void validateStaffAccessToReport(Report report) {
        User currentUser = securityContextService.getCurrentUserOrThrow();

        if (currentUser.isAdmin()) {
            return;
        }

        EmployeeProfile currentEmployee = securityContextService.getCurrentEmployeeProfileOrThrow();

        if (!report.getAuthor().getId().equals(currentEmployee.getId())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "You are not the author of this report"
            );
        }
    }
}