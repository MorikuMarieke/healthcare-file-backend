package com.moriku.healthcare_file_backend.dto.client_profile;

import com.moriku.healthcare_file_backend.model.Sex;

import java.time.Instant;
import java.time.LocalDate;

public class ClientProfileResponse {

    private final Long id;
    private final String bsn;
    private final String firstName;
    private final String lastName;
    private final boolean active;
    private final Instant createdAt;
    private final Long userId;
    private final Sex sex;
    private final LocalDate birthDate;

    public ClientProfileResponse(Long id, String bsn, String firstName, String lastName, Sex sex, LocalDate birthDate,
                                 boolean active, Instant createdAt, Long userId) {
        this.id = id;
        this.bsn = bsn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.birthDate = birthDate;
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

    public Sex getSex() {
        return sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
