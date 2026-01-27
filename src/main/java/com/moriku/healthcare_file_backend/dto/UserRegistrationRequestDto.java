package com.moriku.healthcare_file_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserRegistrationRequestDto {
    @NotBlank
    @Pattern(regexp = "^\\d{9}$", message = "BSN must be exactly 9 digits")
    private String bsn;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
