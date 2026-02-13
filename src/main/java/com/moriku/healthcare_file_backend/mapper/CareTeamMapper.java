package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.careteam.CareTeamClientResponse;
import com.moriku.healthcare_file_backend.dto.careteam.CareTeamMemberResponse;
import com.moriku.healthcare_file_backend.dto.careteam.CareTeamRequest;
import com.moriku.healthcare_file_backend.dto.careteam.CareTeamResponse;
import com.moriku.healthcare_file_backend.model.CareTeam;
import com.moriku.healthcare_file_backend.model.CareTeamClient;
import com.moriku.healthcare_file_backend.model.CareTeamMember;

public final class CareTeamMapper {

    private CareTeamMapper() {
    }

    // CareTeam
    public static CareTeam toEntity(CareTeamRequest request) {
        return new CareTeam(
            request.getTeamName(),
            request.getTeamPhoneNumber(),
            request.getTeamEmail()
        );
    }

    public static void updateEntity(CareTeam entity, CareTeamRequest request) {
        entity.setTeamName(request.getTeamName());
        entity.setTeamPhoneNumber(request.getTeamPhoneNumber());
        entity.setTeamEmail(request.getTeamEmail());
    }

    public static CareTeamResponse toResponse(CareTeam entity) {
        return new CareTeamResponse(
            entity.getId(),
            entity.getTeamName(),
            entity.getTeamPhoneNumber(),
            entity.getTeamEmail()
        );
    }

    // Links
    public static CareTeamMemberResponse toMemberResponse(CareTeamMember member) {
        return new CareTeamMemberResponse(
            member.getId(),
            member.getCareTeam().getId(),
            member.getEmployeeProfile().getId()
        );
    }

    public static CareTeamClientResponse toClientResponse(CareTeamClient link) {
        return new CareTeamClientResponse(
            link.getId(),
            link.getCareTeam().getId(),
            link.getClientProfile().getId()
        );
    }
}
