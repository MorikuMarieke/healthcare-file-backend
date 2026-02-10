package com.moriku.healthcare_file_backend.dto;

import java.time.Instant;

public class UserCreateResponseDto {

    private final Long id;
    private final String email;
    private final String role;
    private final String temporaryPassword;
    private final String firstName;
    private final String lastName;
    private final Instant createdAt;


    public UserCreateResponseDto(Long id, String email, String role, String temporaryPassword, String firstName, String lastName, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.temporaryPassword = temporaryPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getTemporaryPassword() {
        return temporaryPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
