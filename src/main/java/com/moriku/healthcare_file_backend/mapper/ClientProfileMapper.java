package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileCreateRequest;
import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileResponse;
import com.moriku.healthcare_file_backend.model.ClientProfile;

public final class ClientProfileMapper {

    private ClientProfileMapper() {}

    public static ClientProfile toEntity(ClientProfileCreateRequest req) {
        ClientProfile profile = new ClientProfile();
        profile.setBsn(req.getBsn().trim());
        profile.setFirstName(req.getFirstName().trim());
        profile.setLastName(req.getLastName().trim());
        profile.setSex(req.getSex());
        profile.setBirthDate(req.getBirthDate());
        profile.setActive(true);
        return profile;
    }


    public static ClientProfileResponse toResponse(ClientProfile profile) {
        Long userId = profile.getUser() != null ? profile.getUser().getId() : null;

        return new ClientProfileResponse(
            profile.getId(),
            profile.getBsn(),
            profile.getFirstName(),
            profile.getLastName(),
            profile.getSex(),
            profile.getBirthDate(),
            profile.isActive(),
            profile.getCreatedAt(),
            userId
            );
    }
}
