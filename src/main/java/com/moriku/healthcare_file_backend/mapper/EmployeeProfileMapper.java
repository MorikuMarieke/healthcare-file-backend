package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.EmployeeProfileResponseDto;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;

public final class EmployeeProfileMapper {

    private EmployeeProfileMapper() {}

    public static EmployeeProfileResponseDto toResponse(EmployeeProfile profile) {
        return new EmployeeProfileResponseDto(
            profile.getUser().getId(),
            profile.getUser().getEmail(),
            profile.getWorkPhoneNumber(),
            profile.getPersonalPhoneNumber(),
            profile.getPersonalEmail()
        );
    }
}
