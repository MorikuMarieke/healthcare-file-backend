package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.goal.GoalCreateRequest;
import com.moriku.healthcare_file_backend.dto.goal.GoalResponse;
import com.moriku.healthcare_file_backend.dto.goal.GoalUpdateRequest;
import com.moriku.healthcare_file_backend.mapper.GoalMapper;
import com.moriku.healthcare_file_backend.model.CarePlan;
import com.moriku.healthcare_file_backend.model.Goal;
import com.moriku.healthcare_file_backend.repository.CarePlanRepository;
import com.moriku.healthcare_file_backend.repository.GoalRepository;
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

    public GoalService(GoalRepository goalRepository,
                       CarePlanRepository carePlanRepository,
                       MeService meService) {
        this.goalRepository = goalRepository;
        this.carePlanRepository = carePlanRepository;
        this.meService = meService;
    }

    @Transactional
    public GoalResponse create(Long carePlanId, GoalCreateRequest req) {

        validateEvaluationDate(req.getEvaluationDate());

        CarePlan carePlan = carePlanRepository.findById(carePlanId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CarePlan not found"));

        Goal goal = GoalMapper.toEntity(req);
        carePlan.addGoal(goal);

        goal.setLastModifiedAt(Instant.now());
        goal.setLastModifiedByEmployeeId(meService.getMyEmployeeId());

        carePlanRepository.save(carePlan);

        return GoalMapper.toResponse(goal);
    }

    public List<GoalResponse> getAll(Long carePlanId) {
        return goalRepository.findAllByCarePlanId(carePlanId)
            .stream()
            .map(GoalMapper::toResponse)
            .collect(Collectors.toList());
    }

    public GoalResponse getOne(Long carePlanId, Long goalId) {
        Goal goal = goalRepository.findByIdAndCarePlanId(goalId, carePlanId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

        return GoalMapper.toResponse(goal);
    }

    @Transactional
    public GoalResponse update(Long carePlanId, Long goalId, GoalUpdateRequest req) {

        validateEvaluationDate(req.getEvaluationDate());

        Goal goal = goalRepository.findByIdAndCarePlanId(goalId, carePlanId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

        GoalMapper.updateEntity(goal, req);

        goal.setLastModifiedAt(Instant.now());
        goal.setLastModifiedByEmployeeId(meService.getMyEmployeeId());

        Goal saved = goalRepository.save(goal);

        return GoalMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long carePlanId, Long goalId) {

        Goal goal = goalRepository.findByIdAndCarePlanId(goalId, carePlanId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

        CarePlan carePlan = goal.getCarePlan();
        carePlan.removeGoal(goal);

        carePlanRepository.save(carePlan);
    }

    private void validateEvaluationDate(LocalDate evaluationDate) {
        if (evaluationDate == null || !evaluationDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "evaluationDate must be in the future");
        }
    }
}