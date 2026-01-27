package com.moriku.healthcare_file_backend.dto;

import java.time.Instant;

public class ClientProfileResponseDto {

    private final Long id;
    private final String bsn;
    private final String firstName;
    private final String lastName;
    private final boolean active;
    private final Instant createdAt;
    private final Long userId; // nullable -> can be null

    public ClientProfileResponseDto(Long id, String bsn, String firstName, String lastName,
                                    boolean active, Instant createdAt, Long userId) {
        this.id = id;
        this.bsn = bsn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getBsn() {
        return bsn;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Long getUserId() {
        return userId;
    }
}
