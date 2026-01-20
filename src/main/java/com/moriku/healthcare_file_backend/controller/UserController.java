package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.*;
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
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreateResponseDto createUser(@Valid @RequestBody UserCreateRequestDto request) {
        return userService.createUser(request);
    }

    @PatchMapping("/{id}")
    public UserResponseDto patchUser(@PathVariable Long id,
                                     @RequestBody UserUpdateRequestDto request) {
        return userService.patchUser(id, request);
    }

    @PatchMapping("/{id}/password") //TODO: After implementing security, first update {id} to me so only logged in user can change the password.
    public String changePassword(
        @PathVariable Long id,
        @Valid @RequestBody UserPasswordChangeRequestDto request
    ) {
        userService.changePassword(id, request);
        return "Password updated succesfully"; //TODO: update this later with custom exceptions
    }

    @DeleteMapping("/{id}") //TODO: Maybe change to soft delete eventually
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }


}
