package com.moriku.healthcare_file_backend.dto.careteam;

public class CareTeamClientResponse {

    private Long id;
    private Long careTeamId;
    private Long clientProfileId;

    public CareTeamClientResponse(Long id, Long careTeamId, Long clientProfileId) {
        this.id = id;
        this.careTeamId = careTeamId;
        this.clientProfileId = clientProfileId;
    }

    public Long getId() {
        return id;
    }

    public Long getCareTeamId() {
        return careTeamId;
    }

    public Long getClientProfileId() {
        return clientProfileId;
    }
}
