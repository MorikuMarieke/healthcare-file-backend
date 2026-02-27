package com.moriku.healthcare_file_backend.dto.employee_profile;

public class EmployeeProfileResponse {

    private final Long userId;

    private final String workEmail;
    private final String workPhoneNumber;
    private final String personalPhoneNumber;
    private final String personalEmail;

    public EmployeeProfileResponse(Long userId, String workEmail,
                                   String workPhoneNumber, String personalPhoneNumber, String personalEmail) {
        this.userId = userId;
        this.workEmail = workEmail;
        this.workPhoneNumber = workPhoneNumber;
        this.personalPhoneNumber = personalPhoneNumber;
        this.personalEmail = personalEmail;
    }

    public Long getUserId() {
        return userId;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public String getWorkPhoneNumber() {
        return workPhoneNumber;
    }

    public String getPersonalPhoneNumber() {
        return personalPhoneNumber;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }
}
