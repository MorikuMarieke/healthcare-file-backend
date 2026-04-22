package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.goal.GoalCreateRequest;
import com.moriku.healthcare_file_backend.dto.goal.GoalResponse;
import com.moriku.healthcare_file_backend.dto.goal.GoalUpdateRequest;
import com.moriku.healthcare_file_backend.mapper.GoalMapper;
import com.moriku.healthcare_file_backend.model.CarePlan;
import com.moriku.healthcare_file_backend.model.Goal;
import com.moriku.healthcare_file_backend.repository.CarePlanRepository;
import com.moriku.healthcare_file_backend.repository.GoalRepository;
import com.moriku.healthcare_file_backend.security.SecurityContextService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final CarePlanRepository carePlanRepository;
    private final MeService meService;
    private final SecurityContextService securityContextService;

    public GoalService(GoalRepository goalRepository,
                       CarePlanRepository carePlanRepository,
                       MeService meService,
                       SecurityContextService securityContextService) {
        this.goalRepository = goalRepository;
        this.carePlanRepository = carePlanRepository;
        this.meService = meService;
        this.securityContextService = securityContextService;
    }

    @Transactional
    public GoalResponse create(Long clientProfileId, GoalCreateRequest req) {

        validateEvaluationDate(req.getEvaluationDate());

        CarePlan carePlan = getCarePlanByClientProfileId(clientProfileId);

        securityContextService.assertCurrentEmployeeHasAccessToClientForWriteOrThrow(carePlan.getClientProfile());

        Goal goal = GoalMapper.toEntity(req);
        carePlan.addGoal(goal);

        goal.setLastModifiedAt(Instant.now());
        goal.setLastModifiedByEmployeeId(meService.getMyEmployeeId());

        carePlanRepository.save(carePlan);

        return GoalMapper.toResponse(goal);
    }

    public List<GoalResponse> getAll(Long clientProfileId) {
        Long carePlanId = getCarePlanIdByClientProfileId(clientProfileId);

        return goalRepository.findAllByCarePlanId(carePlanId)
            .stream()
            .map(GoalMapper::toResponse)
            .collect(Collectors.toList());
    }

    public GoalResponse getOne(Long clientProfileId, Long goalId) {
        Long carePlanId = getCarePlanIdByClientProfileId(clientProfileId);

        Goal goal = goalRepository.findByIdAndCarePlanId(goalId, carePlanId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

        return GoalMapper.toResponse(goal);
    }

    @Transactional
    public GoalResponse update(Long clientProfileId, Long goalId, GoalUpdateRequest req) {

        validateEvaluationDate(req.getEvaluationDate());

        CarePlan carePlan = getCarePlanByClientProfileId(clientProfileId);

        securityContextService.assertCurrentEmployeeHasAccessToClientForWriteOrThrow(carePlan.getClientProfile());

        Goal goal = goalRepository.findByIdAndCarePlanId(goalId, carePlan.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

        GoalMapper.updateEntity(goal, req);

        goal.setLastModifiedAt(Instant.now());
        goal.setLastModifiedByEmployeeId(meService.getMyEmployeeId());

        Goal saved = goalRepository.save(goal);

        return GoalMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long clientProfileId, Long goalId) {

        CarePlan carePlan = getCarePlanByClientProfileId(clientProfileId);

        securityContextService.assertCurrentEmployeeHasAccessToClientForWriteOrThrow(carePlan.getClientProfile());

        Goal goal = goalRepository.findByIdAndCarePlanId(goalId, carePlan.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

        carePlan.removeGoal(goal);

        carePlanRepository.save(carePlan);
    }

    private CarePlan getCarePlanByClientProfileId(Long clientProfileId) {
        return carePlanRepository.findByClientProfileId(clientProfileId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CarePlan not found"));
    }

    private Long getCarePlanIdByClientProfileId(Long clientProfileId) {
        return getCarePlanByClientProfileId(clientProfileId).getId();
    }

    private void validateEvaluationDate(LocalDate evaluationDate) {
        if (evaluationDate == null || !evaluationDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "evaluationDate must be in the future");
        }
    }

    public List<GoalResponse> getMyGoals() {
        Long clientProfileId = meService.getActiveClientProfileForCurrentClientOrThrow().getId();
        return getAll(clientProfileId);
    }

    public GoalResponse getMyGoalById(Long goalId) {
        Long clientProfileId = meService.getActiveClientProfileForCurrentClientOrThrow().getId();
        return getOne(clientProfileId, goalId);
    }
}