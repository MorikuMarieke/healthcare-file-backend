package com.moriku.healthcare_file_backend.dto.goal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class GoalCreateRequest {

    @NotNull
    private LocalDate evaluationDate;

    @NotBlank
    private String careGoal;

    private String instructions;

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
}