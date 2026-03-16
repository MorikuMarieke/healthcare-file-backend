package com.moriku.healthcare_file_backend.model;

import jakarta.persistence.*;

import java.util.ArrayList;
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

    @Column(nullable = false, length = 5000)
    private String medicalHistory = "";

    @OneToMany(mappedBy = "carePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "carePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

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

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public List<Report> getReports() {
        return reports;
    }
}