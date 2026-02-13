package com.moriku.healthcare_file_backend.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "care_team_clients",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_care_team_client_team_client",
        columnNames = {"care_team_id", "client_profile_id"}
    )
)
public class CareTeamClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "care_team_id", nullable = false)
    private CareTeam careTeam;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_profile_id", nullable = false)
    private ClientProfile clientProfile;

    protected CareTeamClient() {
        // for JPA
    }

    public CareTeamClient(CareTeam careTeam, ClientProfile clientProfile) {
        this.careTeam = careTeam;
        this.clientProfile = clientProfile;
    }

    public Long getId() {
        return id;
    }

    public CareTeam getCareTeam() {
        return careTeam;
    }

    public ClientProfile getClientProfile() {
        return clientProfile;
    }
}
