package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.care_plan.CarePlanResponse;
import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileResponse;
import com.moriku.healthcare_file_backend.dto.employee_profile.EmployeeProfileResponse;
import com.moriku.healthcare_file_backend.dto.report.ReportResponse;
import com.moriku.healthcare_file_backend.dto.report_photo.ReportPhotoResponse;
import com.moriku.healthcare_file_backend.dto.user.UserPasswordChangeRequest;
import com.moriku.healthcare_file_backend.dto.user.UserResponse;
import com.moriku.healthcare_file_backend.model.ReportPhoto;
import com.moriku.healthcare_file_backend.service.CarePlanService;
import com.moriku.healthcare_file_backend.service.MeService;
import com.moriku.healthcare_file_backend.service.ReportPhotoService;
import com.moriku.healthcare_file_backend.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/me")
public class MeController {

    private final MeService meService;
    private final CarePlanService carePlanService;
    private final ReportService reportService;
    private final ReportPhotoService reportPhotoService;

    public MeController(
        MeService meService,
        CarePlanService carePlanService,
        ReportService reportService,
        ReportPhotoService reportPhotoService
    ) {
        this.meService = meService;
        this.carePlanService = carePlanService;
        this.reportService = reportService;
        this.reportPhotoService = reportPhotoService;
    }

    @GetMapping
    public UserResponse getMe() {
        return meService.getMe();
    }

    @GetMapping("/client-profile")
    public ClientProfileResponse getMyClientProfile() {
        return meService.getMyClientProfile();
    }

    @GetMapping("/employee-profile")
    public EmployeeProfileResponse getMyEmployeeProfile() {
        return meService.getMyEmployeeProfile();
    }

    @GetMapping("/care-plan")
    public CarePlanResponse getMyCarePlan() {
        return carePlanService.getMyCarePlan();
    }

    @GetMapping("/reports")
    public List<ReportResponse> getMyReports() {
        return reportService.getMyReports();
    }

    @GetMapping("/reports/{reportId}")
    public ReportResponse getMyReportById(@PathVariable Long reportId) {
        return reportService.getMyReportById(reportId);
    }

    @GetMapping("/reports/{reportId}/photos")
    public List<ReportPhotoResponse> getMyPhotosForReport(@PathVariable Long reportId) {
        return reportPhotoService.getMyPhotosForReport(reportId);
    }

    @GetMapping("/reports/{reportId}/photos/{photoId}/content")
    public ResponseEntity<byte[]> getMyPhotoContent(
        @PathVariable Long reportId,
        @PathVariable Long photoId
    ) {
        ReportPhoto photo = reportPhotoService.getMyReportPhotoById(reportId, photoId);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(photo.getContentType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + photo.getFileName() + "\"")
            .body(photo.getData());
    }

    @PatchMapping("/password")
    public void changeMyPassword(@RequestBody UserPasswordChangeRequest dto) {
        meService.changeMyPassword(dto);
    }
}