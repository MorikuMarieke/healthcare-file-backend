package com.moriku.healthcare_file_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ClientProfileCreateRequestDto {

    @NotBlank
    @Pattern(regexp = "^\\d{9}$", message = "BSN must be exactly 9 digits")
    private String bsn;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
