package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.ClientProfileCreateRequestDto;
import com.moriku.healthcare_file_backend.dto.ClientProfileResponseDto;
import com.moriku.healthcare_file_backend.model.ClientProfile;

public final class ClientProfileMapper {

    private ClientProfileMapper() {}

    public static ClientProfile toEntity(ClientProfileCreateRequestDto req) {
        ClientProfile profile = new ClientProfile();
        profile.setBsn(req.getBsn().trim());
        profile.setFirstName(req.getFirstName().trim());
        profile.setLastName(req.getLastName().trim());
        profile.setActive(true);
        return profile;
    }

    public static ClientProfileResponseDto toResponse(ClientProfile profile) {
        Long userId = profile.getUser() != null ? profile.getUser().getId() : null;

        return new ClientProfileResponseDto(
            profile.getId(),
            profile.getBsn(),
            profile.getFirstName(),
            profile.getLastName(),
            profile.isActive(),
            profile.getCreatedAt(),
            userId
        );
    }
}
