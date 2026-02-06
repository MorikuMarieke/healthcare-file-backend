package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.*;
import com.moriku.healthcare_file_backend.service.AuthService;
import com.moriku.healthcare_file_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserInviteResponseDto createUser(@Valid @RequestBody UserCreateRequestDto dto) {
        return userService.createUser(dto);
    }

    @PostMapping("/auth/invite/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptInvite(@Valid @RequestBody UserInviteAcceptRequestDto dto) {
        authService.acceptInvite(dto);
    }

    @DeleteMapping("/{id}") //TODO: Maybe change to soft delete eventually
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/reset-password")
    public UserPasswordResetResponseDto resetPassword(@PathVariable Long id) {
        return userService.resetPassword(id);
    }
}
