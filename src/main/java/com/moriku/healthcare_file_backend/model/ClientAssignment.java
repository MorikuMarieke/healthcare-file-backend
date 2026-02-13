package com.moriku.healthcare_file_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "client_assignment",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_assignment_employee_client",
        columnNames = {"employee_profile_id", "client_profile_id"}
    )
)
public class ClientAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_profile_id", nullable = false)
    private EmployeeProfile employeeProfile;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_profile_id", nullable = false)
    private ClientProfile clientProfile;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    protected ClientAssignment() {
        // for JPA
    }

    public ClientAssignment(EmployeeProfile employeeProfile, ClientProfile clientProfile, LocalDateTime assignedAt) {
        this.employeeProfile = employeeProfile;
        this.clientProfile = clientProfile;
        this.assignedAt = assignedAt;
    }

    public Long getId() {
        return id;
    }

    public EmployeeProfile getEmployeeProfile() {
        return employeeProfile;
    }

    public ClientProfile getClientProfile() {
        return clientProfile;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
}
