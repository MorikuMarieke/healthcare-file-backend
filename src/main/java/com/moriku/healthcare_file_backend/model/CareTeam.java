package com.moriku.healthcare_file_backend.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "care_teams",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_care_team_email", columnNames = "team_email"),
        @UniqueConstraint(name = "uk_care_team_phone", columnNames = "team_phone_number"),
        @UniqueConstraint(name = "uk_care_team_name", columnNames = "team_name")
    }
)

public class CareTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String teamName;

    @Column(nullable = false, unique = true)
    private String teamPhoneNumber;

    @Column(nullable = false, unique = true)
    private String teamEmail;

    protected CareTeam() {}

    public CareTeam(String teamName, String teamPhoneNumber, String teamEmail) {
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

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamPhoneNumber(String teamPhoneNumber) {
        this.teamPhoneNumber = teamPhoneNumber;
    }

    public void setTeamEmail(String teamEmail) {
        this.teamEmail = teamEmail;
    }
}
