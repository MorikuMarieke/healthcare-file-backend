package com.moriku.healthcare_file_backend.dto;

public class UserResponseDto {

    private final Long id;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String role;

    public UserResponseDto(Long id, String email, String firstName, String lastName, String role) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRole() { return role; }
}
