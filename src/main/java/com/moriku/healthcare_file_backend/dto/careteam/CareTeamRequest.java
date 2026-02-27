package com.moriku.healthcare_file_backend.dto.careteam;

public class CareTeamRequest {

    private String teamName;
    private String teamPhoneNumber;
    private String teamEmail;

    public CareTeamRequest() {
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamPhoneNumber() {
        return teamPhoneNumber;
    }

    public void setTeamPhoneNumber(String teamPhoneNumber) {
        this.teamPhoneNumber = teamPhoneNumber;
    }

    public String getTeamEmail() {
        return teamEmail;
    }

    public void setTeamEmail(String teamEmail) {
        this.teamEmail = teamEmail;
    }
}
