package com.moriku.healthcare_file_backend.dto.careteam;

public class CareTeamResponse {

    private Long id;
    private String teamName;
    private String teamPhoneNumber;
    private String teamEmail;

    public CareTeamResponse(Long id, String teamName, String teamPhoneNumber, String teamEmail) {
        this.id = id;
        this.teamName = teamName;
        this.teamPhoneNumber = teamPhoneNumber;
        this.teamEmail = teamEmail;
    }

    public Long getId() {
        return id;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamPhoneNumber() {
        return teamPhoneNumber;
    }

    public String getTeamEmail() {
        return teamEmail;
    }
}
