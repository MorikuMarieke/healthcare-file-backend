package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.user.*;
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
    public UserResponse register(@Valid @RequestBody UserRegistrationRequest request) {
        return authService.registerClient(request);
    }

    @PostMapping("/login")
    public UserLoginResponse login(@Valid @RequestBody UserLoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/invite/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptInvite(@RequestParam String token, @Valid @RequestBody UserInviteAcceptRequest dto) {
        authService.acceptInvite(token, dto);
    }

}