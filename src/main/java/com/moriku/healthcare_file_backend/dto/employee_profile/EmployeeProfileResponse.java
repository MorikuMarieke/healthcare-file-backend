package com.moriku.healthcare_file_backend.dto.employee_profile;

public class EmployeeProfileResponse {

    private final Long id;
    private final String workPhoneNumber;
    private final String personalPhoneNumber;
    private final String personalEmail;
    private final String firstName;
    private final String lastName;
    private final Long careTeamId;

    public EmployeeProfileResponse(
        Long id,
        String workPhoneNumber,
        String personalPhoneNumber,
        String personalEmail,
        String firstName,
        String lastName,
        Long careTeamId
    ) {
        this.id = id;
        this.workPhoneNumber = workPhoneNumber;
        this.personalPhoneNumber = personalPhoneNumber;
        this.personalEmail = personalEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.careTeamId = careTeamId;
    }

    public Long getId() {
        return id;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getCareTeamId() {
        return careTeamId;
    }
}