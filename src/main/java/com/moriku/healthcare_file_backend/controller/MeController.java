package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.ClientProfileResponseDto;
import com.moriku.healthcare_file_backend.dto.EmployeeProfileResponseDto;
import com.moriku.healthcare_file_backend.dto.UserPasswordChangeRequestDto;
import com.moriku.healthcare_file_backend.dto.UserResponseDto;
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
    public UserResponseDto me() {
        return meService.getMe();
    }

    @GetMapping("/client-profile")
    public ClientProfileResponseDto myClientProfile() {
        return meService.getMyClientProfile();
    }

    @GetMapping("/employee-profile")
    public EmployeeProfileResponseDto myEmployeeProfile() {
        return meService.getMyEmployeeProfile();
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeMyPassword(@Valid @RequestBody UserPasswordChangeRequestDto request) {
        meService.changeMyPassword(request);
    }
}
