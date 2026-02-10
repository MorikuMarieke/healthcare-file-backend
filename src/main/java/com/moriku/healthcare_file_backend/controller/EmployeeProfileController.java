package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.EmployeeProfileResponseDto;
import com.moriku.healthcare_file_backend.dto.EmployeeProfileUpdateRequestDto;
import com.moriku.healthcare_file_backend.service.EmployeeProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee-profiles")
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    public EmployeeProfileController(EmployeeProfileService employeeProfileService) {
        this.employeeProfileService = employeeProfileService;
    }

    @GetMapping("/{userId}")
    public EmployeeProfileResponseDto getEmployeeProfile(@PathVariable Long userId) {
        return employeeProfileService.getEmployeeProfile(userId);
    }

    @PatchMapping("/{userId}")
    public EmployeeProfileResponseDto patchEmployeeProfile(
        @PathVariable Long userId,
        @Valid @RequestBody EmployeeProfileUpdateRequestDto request
    ) {
        return employeeProfileService.patchEmployeeProfile(userId, request);
    }

}
