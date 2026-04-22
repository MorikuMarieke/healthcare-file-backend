package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.employee_profile.EmployeeProfileResponse;
import com.moriku.healthcare_file_backend.dto.employee_profile.EmployeeProfileUpdateRequest;
import com.moriku.healthcare_file_backend.service.EmployeeProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee-profiles")
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    public EmployeeProfileController(EmployeeProfileService employeeProfileService) {
        this.employeeProfileService = employeeProfileService;
    }

    @GetMapping("/{userId}")
    public EmployeeProfileResponse getEmployeeProfile(@PathVariable Long userId) {
        return employeeProfileService.getEmployeeProfile(userId);
    }

    @PatchMapping("/{userId}")
    public EmployeeProfileResponse patchEmployeeProfile(
        @PathVariable Long userId,
        @Valid @RequestBody EmployeeProfileUpdateRequest request
    ) {
        return employeeProfileService.patchEmployeeProfile(userId, request);
    }

    @PatchMapping("/{userId}/care-team/{teamId}")
    public EmployeeProfileResponse assignEmployeeToTeam(
        @PathVariable Long userId,
        @PathVariable Long teamId
    ) {
        return employeeProfileService.assignEmployeeToTeam(userId, teamId);
    }

}
