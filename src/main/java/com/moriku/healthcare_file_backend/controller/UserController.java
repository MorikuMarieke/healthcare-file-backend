package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.user.UserCreateRequest;
import com.moriku.healthcare_file_backend.dto.user.UserInviteResponse;
import com.moriku.healthcare_file_backend.dto.user.UserPasswordResetResponse;
import com.moriku.healthcare_file_backend.dto.user.UserResponse;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserInviteResponse createUser(@Valid @RequestBody UserCreateRequest dto) {
        return userService.createUser(dto);
    }

    @DeleteMapping("/{id}") //TODO: Maybe change to soft delete eventually
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/reset-password")
    public UserPasswordResetResponse resetPassword(@PathVariable Long id) {
        return userService.resetPassword(id);
    }
}
