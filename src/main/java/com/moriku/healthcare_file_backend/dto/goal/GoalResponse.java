package com.moriku.healthcare_file_backend.dto.goal;

import java.time.Instant;
import java.time.LocalDate;

public class GoalResponse {

    private Long id;
    private Long carePlanId;
    private LocalDate dateCreated;
    private LocalDate evaluationDate;
    private String careGoal;
    private String instructions;
    private Instant lastModifiedAt;
    private Long lastModifiedByEmployeeId;

    public GoalResponse(Long id,
                        Long carePlanId,
                        LocalDate dateCreated,
                        LocalDate evaluationDate,
                        String careGoal,
                        String instructions,
                        Instant lastModifiedAt,
                        Long lastModifiedByEmployeeId) {
        this.id = id;
        this.carePlanId = carePlanId;
        this.dateCreated = dateCreated;
        this.evaluationDate = evaluationDate;
        this.careGoal = careGoal;
        this.instructions = instructions;
        this.lastModifiedAt = lastModifiedAt;
        this.lastModifiedByEmployeeId = lastModifiedByEmployeeId;
    }

    public Long getId() {
        return id;
    }

    public Long getCarePlanId() {
        return carePlanId;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public String getCareGoal() {
        return careGoal;
    }

    public String getInstructions() {
        return instructions;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public Long getLastModifiedByEmployeeId() {
        return lastModifiedByEmployeeId;
    }
}