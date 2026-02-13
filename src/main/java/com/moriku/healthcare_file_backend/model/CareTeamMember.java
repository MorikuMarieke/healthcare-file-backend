package com.moriku.healthcare_file_backend.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "care_team_members",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_care_team_member_team_employee",
        columnNames = {"care_team_id", "employee_profile_id"}
    )
)
public class CareTeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "care_team_id", nullable = false)
    private CareTeam careTeam;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_profile_id", nullable = false)
    private EmployeeProfile employeeProfile;

    protected CareTeamMember() {}

    public CareTeamMember(CareTeam careTeam, EmployeeProfile employeeProfile) {
        this.careTeam = careTeam;
        this.employeeProfile = employeeProfile;
    }

    public Long getId() { return id; }
    public CareTeam getCareTeam() { return careTeam; }
    public EmployeeProfile getEmployeeProfile() { return employeeProfile; }
}
