package com.moriku.healthcare_file_backend.dto.careteam;

public class CareTeamMemberResponse {

    private Long id;
    private Long careTeamId;
    private Long employeeProfileId;

    public CareTeamMemberResponse(Long id, Long careTeamId, Long employeeProfileId) {
        this.id = id;
        this.careTeamId = careTeamId;
        this.employeeProfileId = employeeProfileId;
    }

    public Long getId() {
        return id;
    }

    public Long getCareTeamId() {
        return careTeamId;
    }

    public Long getEmployeeProfileId() {
        return employeeProfileId;
    }
}