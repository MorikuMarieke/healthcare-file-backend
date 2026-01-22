package com.moriku.healthcare_file_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserCreateRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^(EMPLOYEE|ADMIN)$", message = "Role must be EMPLOYEE or ADMIN")
    private String role;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    // optional employee profile details (only if you want to create profile in same request)
    private String workPhoneNumber;
    private String personalPhoneNumber;
    private String personalEmail;//TODO: Double ckeck if these values are necessary here or if i will just do this as a patch of client profile, i think currently there is no setup in the requestDTO.

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public @NotBlank String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank String lastName) {
        this.lastName = lastName;
    }

    public String getWorkPhoneNumber() {
        return workPhoneNumber;
    }

    public void setWorkPhoneNumber(String workPhoneNumber) {
        this.workPhoneNumber = workPhoneNumber;
    }

    public String getPersonalPhoneNumber() {
        return personalPhoneNumber;
    }

    public void setPersonalPhoneNumber(String personalPhoneNumber) {
        this.personalPhoneNumber = personalPhoneNumber;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }
}
