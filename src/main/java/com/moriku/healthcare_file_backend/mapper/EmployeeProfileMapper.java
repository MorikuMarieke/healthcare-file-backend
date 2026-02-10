package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.employee_profile.EmployeeProfileResponse;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;

public final class EmployeeProfileMapper {

    private EmployeeProfileMapper() {}

    public static EmployeeProfileResponse toResponse(EmployeeProfile profile) {
        return new EmployeeProfileResponse(
            profile.getUser().getId(),
            profile.getUser().getEmail(),
            profile.getWorkPhoneNumber(),
            profile.getPersonalPhoneNumber(),
            profile.getPersonalEmail()
        );
    }
}
