package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.report_photo.ReportPhotoResponse;
import com.moriku.healthcare_file_backend.mapper.ReportPhotoMapper;
import com.moriku.healthcare_file_backend.model.CarePlan;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.Report;
import com.moriku.healthcare_file_backend.model.ReportPhoto;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.CareTeamMemberRepository;
import com.moriku.healthcare_file_backend.repository.ReportPhotoRepository;
import com.moriku.healthcare_file_backend.repository.ReportRepository;
import com.moriku.healthcare_file_backend.security.SecurityContextService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final CareTeamMemberRepository careTeamMemberRepository;
    private final SecurityContextService securityContextService;
    private final CarePlanService carePlanService;

    public ReportPhotoServiceImpl(
        ReportPhotoRepository reportPhotoRepository,
        ReportRepository reportRepository,
        CareTeamMemberRepository careTeamMemberRepository,
        SecurityContextService securityContextService,
        CarePlanService carePlanService
    ) {
        this.reportPhotoRepository = reportPhotoRepository;
        this.reportRepository = reportRepository;
        this.careTeamMemberRepository = careTeamMemberRepository;
        this.securityContextService = securityContextService;
        this.carePlanService = carePlanService;
    }

    @Override
    @Transactional
    public ReportPhotoResponse uploadPhotoToReport(Long reportId, MultipartFile file) {
        Report report = getReportByIdOrThrow(reportId);

        validateReportPhotoUploadAccess(report);
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
    @Transactional(readOnly = true)
    public List<ReportPhotoResponse> getAllPhotosForReport(Long reportId) {
        getReportByIdOrThrow(reportId);
        validateStaffReadAccess();

        List<ReportPhoto> reportPhotos = reportPhotoRepository.findAllByReportIdOrderByUploadedAtAsc(reportId);
        return ReportPhotoMapper.toResponseList(reportPhotos);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportPhoto getReportPhotoById(Long photoId) {
        ReportPhoto reportPhoto = getReportPhotoByIdOrThrow(photoId);
        validateStaffReadAccess();
        return reportPhoto;
    }

    @Override
    @Transactional
    public void deleteReportPhotoById(Long photoId) {
        ReportPhoto reportPhoto = getReportPhotoByIdOrThrow(photoId);
        validateReportAuthorAccess(reportPhoto.getReport());

        reportPhotoRepository.delete(reportPhoto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportPhotoResponse> getMyPhotosForReport(Long reportId) {
        Report report = getMyReportOrThrow(reportId);

        List<ReportPhoto> reportPhotos = reportPhotoRepository.findAllByReportIdOrderByUploadedAtAsc(report.getId());
        return ReportPhotoMapper.toResponseList(reportPhotos);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportPhoto getMyReportPhotoById(Long reportId, Long photoId) {
        Report report = getMyReportOrThrow(reportId);

        return reportPhotoRepository.findByIdAndReportId(photoId, report.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report photo not found."));
    }

    private Report getReportByIdOrThrow(Long reportId) {
        return reportRepository.findById(reportId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found."));
    }

    private ReportPhoto getReportPhotoByIdOrThrow(Long photoId) {
        return reportPhotoRepository.findById(photoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report photo not found."));
    }

    private Report getMyReportOrThrow(Long reportId) {
        CarePlan myCarePlan = carePlanService.getMyCarePlanEntityOrThrow();

        return reportRepository.findByIdAndCarePlanId(reportId, myCarePlan.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found."));
    }

    private void validateUploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required.");
        }

        String contentType = file.getContentType();
        if (contentType == null
            || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPEG and PNG images are allowed.");
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

    private void validateStaffReadAccess() {
        User currentUser = securityContextService.getCurrentUserOrThrow();

        if (currentUser.isAdmin() || currentUser.isEmployee()) {
            return;
        }

        throw new ResponseStatusException(
            HttpStatus.FORBIDDEN,
            "Only ADMIN or EMPLOYEE can view report photos."
        );
    }

    private void validateReportPhotoUploadAccess(Report report) {
        EmployeeProfile currentEmployee = securityContextService.getCurrentEmployeeProfileOrThrow();

        assertEmployeeHasAccessToReportClientOrThrow(currentEmployee, report);
        validateReportAuthorAccess(report);
    }

    private void validateReportAuthorAccess(Report report) {
        EmployeeProfile currentEmployee = securityContextService.getCurrentEmployeeProfileOrThrow();

        if (!report.getAuthor().getId().equals(currentEmployee.getId())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "You are not the author of this report."
            );
        }
    }

    private void assertEmployeeHasAccessToReportClientOrThrow(EmployeeProfile employee, Report report) {
        Long careTeamId = report.getCarePlan().getClientProfile().getCareTeam().getId();

        boolean allowed = careTeamMemberRepository.existsByCareTeamIdAndEmployeeProfileId(careTeamId, employee.getId());

        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this client.");
        }
    }
}