package com.moriku.healthcare_file_backend.dto;

public class UserRegistrationResponseDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;

    public UserRegistrationResponseDto(Long id, String email, String firstName, String lastName, String role) {
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
