package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileResponse;
import com.moriku.healthcare_file_backend.dto.employee_profile.EmployeeProfileResponse;
import com.moriku.healthcare_file_backend.dto.user.UserPasswordChangeRequest;
import com.moriku.healthcare_file_backend.dto.user.UserResponse;
import com.moriku.healthcare_file_backend.service.MeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
public class MeController {

    private final MeService meService;

    public MeController(MeService meService) {
        this.meService = meService;
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
}
