package com.moriku.healthcare_file_backend.dto;

public class UserCreateResponseDto {

    private final Long id;
    private final String email;
    private final String role;
    private final String temporaryPassword;
    private final boolean mustChangePassword;
    private final String firstName;
    private final String lastName;

    public UserCreateResponseDto(Long id, String email, String role, String temporaryPassword, boolean mustChangePassword, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.temporaryPassword = temporaryPassword;
        this.mustChangePassword = mustChangePassword;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
