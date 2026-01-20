package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.UserRegistrationRequestDto;
import com.moriku.healthcare_file_backend.dto.UserResponseDto;
import com.moriku.healthcare_file_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto request) {
        return authService.registerClient(request);
    }

//    @PostMapping("/register/employee")
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserResponseDto registerEmployee(@Valid @RequestBody UserRegistrationRequestDto request) {
//        return authService.registerEmployee(request);
//    }
//
//    @PostMapping("/register/admin")
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserResponseDto registerAdmin(@Valid @RequestBody UserRegistrationRequestDto request) {
//        return authService.registerAdmin(request);
//    }
}
