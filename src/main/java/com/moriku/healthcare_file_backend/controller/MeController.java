package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.care_plan.CarePlanResponse;
import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileResponse;
import com.moriku.healthcare_file_backend.dto.client_profile.ContactDetailsResponse;
import com.moriku.healthcare_file_backend.dto.employee_profile.EmployeeProfileResponse;
import com.moriku.healthcare_file_backend.dto.goal.GoalResponse;
import com.moriku.healthcare_file_backend.dto.report.ReportResponse;
import com.moriku.healthcare_file_backend.dto.report_photo.ReportPhotoResponse;
import com.moriku.healthcare_file_backend.dto.user.UserPasswordChangeRequest;
import com.moriku.healthcare_file_backend.dto.user.UserResponse;
import com.moriku.healthcare_file_backend.model.ReportPhoto;
import com.moriku.healthcare_file_backend.service.*;
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
    private final ClientProfileService clientProfileService;
    private final GoalService goalService;

    public MeController(
        MeService meService,
        CarePlanService carePlanService,
        ReportService reportService,
        ReportPhotoService reportPhotoService,
        ClientProfileService clientProfileService, GoalService goalService) {
        this.meService = meService;
        this.carePlanService = carePlanService;
        this.reportService = reportService;
        this.reportPhotoService = reportPhotoService;
        this.clientProfileService = clientProfileService;
        this.goalService = goalService;
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

    @GetMapping("/client-profile/contact-details")
    public ContactDetailsResponse getMyContactDetails()
    {
        return clientProfileService.getMyContactDetails();
    }

    @GetMapping("client-profile/care-plan")
    public CarePlanResponse getMyCarePlan() {
        return carePlanService.getMyCarePlan();
    }

    @GetMapping("client-profile/care-plan/goals")
    public List<GoalResponse> getMyGoals() {
        return goalService.getMyGoals();
    }

    @GetMapping("client-profile/care-plan/goals/{goalId}")
    public GoalResponse getMyGoalById(@PathVariable Long goalId) {
        return goalService.getMyGoalById(goalId);
    }

    @GetMapping("client-profile/care-plan/reports")
    public List<ReportResponse> getMyReports() {
        return reportService.getMyReports();
    }

    @GetMapping("client-profile/care-plan/reports/{reportId}")
    public ReportResponse getMyReportById(@PathVariable Long reportId) {
        return reportService.getMyReportById(reportId);
    }

    @GetMapping("client-profile/care-plan/reports/{reportId}/photos")
    public List<ReportPhotoResponse> getMyPhotosForReport(@PathVariable Long reportId) {
        return reportPhotoService.getMyPhotosForReport(reportId);
    }

    @GetMapping("client-profile/care-plan/reports/{reportId}/photos/{photoId}/content")
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