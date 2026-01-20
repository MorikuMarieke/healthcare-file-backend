package com.moriku.healthcare_file_backend.dto;

public class UserCreateResponseDto {
    private final Long id;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String role;
    private final String temporaryPassword;
    private final boolean mustChangePassword;

    public UserCreateResponseDto(Long id, String email, String firstName, String lastName, String role, String temporaryPassword, boolean mustChangePassword) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.temporaryPassword = temporaryPassword;
        this.mustChangePassword = mustChangePassword;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public String getTemporaryPassword() {
        return temporaryPassword;
    }

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

}
