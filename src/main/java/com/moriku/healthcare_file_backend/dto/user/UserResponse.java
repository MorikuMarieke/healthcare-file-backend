package com.moriku.healthcare_file_backend.dto.user;

import java.time.Instant;

public class UserResponse {

    private final Long id;
    private final String email;
    private final String role;
    private final Instant createdAt;

    public UserResponse(Long id, String email, String role, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.role = role;
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

    public Instant getCreatedAt() {
        return createdAt;
    }
}
