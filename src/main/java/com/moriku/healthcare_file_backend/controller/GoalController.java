package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.goal.GoalCreateRequest;
import com.moriku.healthcare_file_backend.dto.goal.GoalResponse;
import com.moriku.healthcare_file_backend.dto.goal.GoalUpdateRequest;
import com.moriku.healthcare_file_backend.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/care-plans/{carePlanId}/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GoalResponse create(@PathVariable Long carePlanId,
                               @Valid @RequestBody GoalCreateRequest req) {
        return goalService.create(carePlanId, req);
    }

    @GetMapping
    public List<GoalResponse> getAll(@PathVariable Long carePlanId) {
        return goalService.getAll(carePlanId);
    }

    @GetMapping("/{goalId}")
    public GoalResponse getOne(@PathVariable Long carePlanId,
                               @PathVariable Long goalId) {
        return goalService.getOne(carePlanId, goalId);
    }

    @PutMapping("/{goalId}")
    public GoalResponse update(@PathVariable Long carePlanId,
                               @PathVariable Long goalId,
                               @Valid @RequestBody GoalUpdateRequest req) {
        return goalService.update(carePlanId, goalId, req);
    }

    @DeleteMapping("/{goalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long carePlanId,
                       @PathVariable Long goalId) {
        goalService.delete(carePlanId, goalId);
    }
}