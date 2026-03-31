package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.employee_profile.EmployeeProfileResponse;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;

public final class EmployeeProfileMapper {

    private EmployeeProfileMapper() {}

    public static EmployeeProfileResponse toResponse(EmployeeProfile profile) {
        Long careTeamId = profile.getCareTeam() != null ? profile.getCareTeam().getId() : null;

        return new EmployeeProfileResponse(
            profile.getId(),
            profile.getWorkPhoneNumber(),
            profile.getPersonalPhoneNumber(),
            profile.getPersonalEmail(),
            profile.getFirstName(),
            profile.getLastName(),
            careTeamId
        );
    }
}
