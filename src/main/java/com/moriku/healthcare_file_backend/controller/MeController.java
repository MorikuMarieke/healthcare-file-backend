package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileResponse;
import com.moriku.healthcare_file_backend.dto.employee_profile.EmployeeProfileResponse;
import com.moriku.healthcare_file_backend.dto.goal.GoalResponse;
import com.moriku.healthcare_file_backend.dto.report.ReportResponse;
import com.moriku.healthcare_file_backend.dto.user.UserPasswordChangeRequest;
import com.moriku.healthcare_file_backend.dto.user.UserResponse;
import com.moriku.healthcare_file_backend.service.GoalService;
import com.moriku.healthcare_file_backend.service.MeService;
import com.moriku.healthcare_file_backend.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/me")
public class MeController {

    private final MeService meService;
    private final ReportService reportService;
    private final GoalService goalService;

    public MeController(MeService meService, ReportService reportService, GoalService goalService) {
        this.meService = meService;
        this.reportService = reportService;
        this.goalService = goalService;
    }

    @GetMapping
    public UserResponse me() {
        return meService.getMe();
    }

    @GetMapping("/client-profile")
    public ClientProfileResponse myClientProfile() {
        return meService.getMyClientProfile();
    }

    @GetMapping("/employee-profile")
    public EmployeeProfileResponse myEmployeeProfile() {
        return meService.getMyEmployeeProfile();
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeMyPassword(@Valid @RequestBody UserPasswordChangeRequest request) {
        meService.changeMyPassword(request);
    }

    // Reports
    @GetMapping("/reports")
    public ResponseEntity<List<ReportResponse>> getMyReports() {
        return ResponseEntity.ok(reportService.getMyReports());
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<ReportResponse> getMyReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getMyReportById(id));
    }

    // Goals
    @GetMapping("/care-plan/goals")
    public ResponseEntity<List<GoalResponse>> getMyGoals() {
        return ResponseEntity.ok(goalService.getMyGoals());
    }

    @GetMapping("/care-plan/goals/{id}")
    public ResponseEntity<GoalResponse> getMyGoalById(@PathVariable Long id) {
        return ResponseEntity.ok(goalService.getMyGoalById(id));
    }
}