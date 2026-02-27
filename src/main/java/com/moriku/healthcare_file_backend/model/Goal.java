package com.moriku.healthcare_file_backend.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "care_plan_id", nullable = false)
    private CarePlan carePlan;

    @Column(name = "date_created", nullable = false, updatable = false)
    private LocalDate dateCreated;

    @Column(name = "evaluation_date", nullable = false)
    private LocalDate evaluationDate;

    @Column(name = "care_goal", nullable = false, columnDefinition = "text")
    private String careGoal;

    @Column(columnDefinition = "text")
    private String instructions;

    // audit on goals (container blijft simpel)
    @Column(name = "last_modified_at", nullable = false)
    private Instant lastModifiedAt;

    // simpel & examen-proof: alleen id opslaan (nullable)
    @Column(name = "last_modified_by_employee_id")
    private Long lastModifiedByEmployeeId;

    @PrePersist
    void prePersist() {
        this.dateCreated = LocalDate.now();
        this.lastModifiedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public CarePlan getCarePlan() {
        return carePlan;
    }

    public void setCarePlan(CarePlan carePlan) {
        this.carePlan = carePlan;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getCareGoal() {
        return careGoal;
    }

    public void setCareGoal(String careGoal) {
        this.careGoal = careGoal;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public Long getLastModifiedByEmployeeId() {
        return lastModifiedByEmployeeId;
    }

    public void setLastModifiedByEmployeeId(Long lastModifiedByEmployeeId) {
        this.lastModifiedByEmployeeId = lastModifiedByEmployeeId;
    }
}