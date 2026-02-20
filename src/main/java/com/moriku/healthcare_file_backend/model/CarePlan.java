package com.moriku.healthcare_file_backend.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "care_plans",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_care_plan_client_profile", columnNames = {"client_profile_id"})
    }
)
public class CarePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_profile_id", nullable = false, unique = true)
    private ClientProfile clientProfile;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false, length = 2000)
    private String notes = "";

    public CarePlan() {
    }

    public CarePlan(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public ClientProfile getClientProfile() {
        return clientProfile;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setClientProfile(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
