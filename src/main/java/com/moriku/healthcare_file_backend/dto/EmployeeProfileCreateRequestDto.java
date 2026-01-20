package com.moriku.healthcare_file_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class EmployeeProfileCreateRequestDto {

    @NotBlank
    private String workPhoneNumber;

    @NotBlank
    private String personalPhoneNumber;

    private String personalEmail;

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
