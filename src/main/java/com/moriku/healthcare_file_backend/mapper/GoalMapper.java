package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.goal.GoalCreateRequest;
import com.moriku.healthcare_file_backend.dto.goal.GoalResponse;
import com.moriku.healthcare_file_backend.dto.goal.GoalUpdateRequest;
import com.moriku.healthcare_file_backend.model.Goal;

public class GoalMapper {

    private GoalMapper() {
    }

    public static Goal toEntity(GoalCreateRequest req) {
        Goal goal = new Goal();
        goal.setEvaluationDate(req.getEvaluationDate());
        goal.setCareGoal(req.getCareGoal());
        goal.setInstructions(req.getInstructions());
        return goal;
    }

    public static void updateEntity(Goal goal, GoalUpdateRequest req) {
        goal.setEvaluationDate(req.getEvaluationDate());
        goal.setCareGoal(req.getCareGoal());
        goal.setInstructions(req.getInstructions());
    }

    public static GoalResponse toResponse(Goal goal) {
        return new GoalResponse(
            goal.getId(),
            goal.getCarePlan().getId(),
            goal.getDateCreated(),
            goal.getEvaluationDate(),
            goal.getCareGoal(),
            goal.getInstructions(),
            goal.getLastModifiedAt(),
            goal.getLastModifiedByEmployeeId()
        );
    }
}