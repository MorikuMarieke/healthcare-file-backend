package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.UserCreateRequestDto;
import com.moriku.healthcare_file_backend.dto.UserRegistrationResponseDto;
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
    public List<UserRegistrationResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserRegistrationResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponseDto createUser(@Valid @RequestBody UserCreateRequestDto request) {
        return userService.createUser(request);
    }

}
