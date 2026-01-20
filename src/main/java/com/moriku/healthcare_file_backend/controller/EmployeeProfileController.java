package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.EmployeeProfileCreateRequestDto;
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

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeProfileResponseDto createEmployeeProfile(
        @PathVariable Long userId,
        @Valid @RequestBody EmployeeProfileCreateRequestDto request
    ) {
        return employeeProfileService.createEmployeeProfile(userId, request);
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

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeProfile(@PathVariable Long userId) {
        employeeProfileService.deleteEmployeeProfile(userId);
    }
}
