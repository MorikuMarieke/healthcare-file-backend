package com.moriku.healthcare_file_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class UserUpdateRequestDto {

    // 9 digits if provided
    @Pattern(regexp = "^[0-9]{9}$", message = "BSN must be exactly 9 digits")
    private String bsn;

    @Email
    private String email;

    private String password;
    private String firstName;
    private String lastName;

    public String getBsn() { return bsn; }
    public void setBsn(String bsn) { this.bsn = bsn; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
