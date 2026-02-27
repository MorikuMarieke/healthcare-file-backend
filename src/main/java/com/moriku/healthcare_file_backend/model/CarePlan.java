package com.moriku.healthcare_file_backend.model;

import jakarta.persistence.*;

import java.util.List;

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

    @Column(nullable = false, length = 2000)
    private String notes = "";

    @OneToMany(mappedBy = "carePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals = new java.util.ArrayList<>();

    public CarePlan() {
    }

    public CarePlan(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
        goal.setCarePlan(this);
    }

    public void removeGoal(Goal goal) {
        goals.remove(goal);
        goal.setCarePlan(null);
    }

    public Long getId() {
        return id;
    }

    public ClientProfile getClientProfile() {
        return clientProfile;
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

    public List<Goal> getGoals() {
        return goals;
    }
}
