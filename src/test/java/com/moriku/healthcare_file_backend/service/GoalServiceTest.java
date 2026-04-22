package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.goal.GoalCreateRequest;
import com.moriku.healthcare_file_backend.dto.goal.GoalUpdateRequest;
import com.moriku.healthcare_file_backend.model.CarePlan;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.Goal;
import com.moriku.healthcare_file_backend.repository.CarePlanRepository;
import com.moriku.healthcare_file_backend.repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private CarePlanRepository carePlanRepository;

    @Mock
    private MeService meService;

    @InjectMocks
    private GoalService goalService;

    private ClientProfile clientProfile;
    private CarePlan carePlan;
    private Goal goal;

    @BeforeEach
    void setUp() {
        clientProfile = new ClientProfile();
        carePlan = new CarePlan();
        carePlan.setClientProfile(clientProfile);

        goal = new Goal();
        goal.setCarePlan(carePlan);
    }

    @Test
    void getAll_shouldReturnGoalResponses() {
        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.of(carePlan));
        when(goalRepository.findAllByCarePlanId(any())).thenReturn(List.of(goal));

        List<?> result = goalService.getAll(1L);

        assertEquals(1, result.size());
        verify(carePlanRepository).findByClientProfileId(1L);
        verify(goalRepository).findAllByCarePlanId(any());
    }

    @Test
    void getAll_shouldReturnEmptyListWhenNoGoalsExist() {
        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.of(carePlan));
        when(goalRepository.findAllByCarePlanId(any())).thenReturn(List.of());

        var result = goalService.getAll(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(carePlanRepository).findByClientProfileId(1L);
        verify(goalRepository).findAllByCarePlanId(any());
    }

    @Test
    void getAll_shouldThrowWhenCarePlanNotFound() {
        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> goalService.getAll(1L));
    }

    @Test
    void getOne_shouldReturnGoal() {
        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.of(carePlan));
        when(goalRepository.findByIdAndCarePlanId(eq(1L), any())).thenReturn(Optional.of(goal));

        var result = goalService.getOne(1L, 1L);

        assertNotNull(result);
        verify(goalRepository).findByIdAndCarePlanId(eq(1L), any());
    }

    @Test
    void getOne_shouldThrowWhenGoalNotFound() {
        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.of(carePlan));
        when(goalRepository.findByIdAndCarePlanId(eq(99L), any())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> goalService.getOne(1L, 99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Goal not found", exception.getReason());
        verify(carePlanRepository).findByClientProfileId(1L);
        verify(goalRepository).findByIdAndCarePlanId(eq(99L), any());
    }

    @Test
    void create_shouldCreateGoal() {
        GoalCreateRequest req = new GoalCreateRequest();
        req.setEvaluationDate(LocalDate.now().plusDays(1));

        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.of(carePlan));
        when(meService.getMyEmployeeId()).thenReturn(1L);
        when(carePlanRepository.save(any(CarePlan.class))).thenReturn(carePlan);

        var result = goalService.create(1L, req);

        assertNotNull(result);

        verify(carePlanRepository).findByClientProfileId(1L);
        verify(carePlanRepository).save(carePlan);
    }

    @Test
    void create_shouldThrowWhenEvaluationDateIsToday() {
        GoalCreateRequest req = new GoalCreateRequest();
        req.setEvaluationDate(LocalDate.now());

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> goalService.create(1L, req)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("evaluationDate must be in the future", exception.getReason());

        verify(carePlanRepository, never()).findByClientProfileId(any());
        verify(carePlanRepository, never()).save(any(CarePlan.class));
    }

    @Test
    void update_shouldUpdateGoal() {
        GoalUpdateRequest req = new GoalUpdateRequest();
        req.setEvaluationDate(LocalDate.now().plusDays(1));

        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.of(carePlan));
        when(goalRepository.findByIdAndCarePlanId(eq(1L), any())).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);
        when(meService.getMyEmployeeId()).thenReturn(1L);

        var result = goalService.update(1L, 1L, req);

        assertNotNull(result);

        verify(goalRepository).findByIdAndCarePlanId(eq(1L), any());
        verify(goalRepository).save(goal);
    }

    @Test
    void update_shouldThrowWhenGoalNotFound() {
        GoalUpdateRequest req = new GoalUpdateRequest();
        req.setEvaluationDate(LocalDate.now().plusDays(1));

        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.of(carePlan));
        when(goalRepository.findByIdAndCarePlanId(eq(99L), any())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> goalService.update(1L, 99L, req)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Goal not found", exception.getReason());
    }

    @Test
    void update_shouldThrowWhenEvaluationDateInvalid() {
        GoalUpdateRequest req = new GoalUpdateRequest();
        req.setEvaluationDate(LocalDate.now());

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> goalService.update(1L, 1L, req)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("evaluationDate must be in the future", exception.getReason());

        verify(carePlanRepository, never()).findByClientProfileId(any());
        verify(goalRepository, never()).findByIdAndCarePlanId(any(), any());
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    void delete_shouldRemoveGoal() {
        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.of(carePlan));
        when(goalRepository.findByIdAndCarePlanId(eq(1L), any())).thenReturn(Optional.of(goal));

        goalService.delete(1L, 1L);

        verify(goalRepository).findByIdAndCarePlanId(eq(1L), any());
        verify(carePlanRepository).save(carePlan);
    }

    @Test
    void delete_shouldThrowWhenGoalNotFound() {
        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.of(carePlan));
        when(goalRepository.findByIdAndCarePlanId(eq(99L), any())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> goalService.delete(1L, 99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Goal not found", exception.getReason());
    }

    @Test
    void getMyGoals_shouldReturnGoals() {
        when(meService.getActiveClientProfileForCurrentClientOrThrow()).thenReturn(clientProfile);
        when(carePlanRepository.findByClientProfileId(any())).thenReturn(Optional.of(carePlan));
        when(goalRepository.findAllByCarePlanId(any())).thenReturn(List.of(goal));

        var result = goalService.getMyGoals();

        assertEquals(1, result.size());
    }

    @Test
    void getMyGoalById_shouldReturnGoal() {
        when(meService.getActiveClientProfileForCurrentClientOrThrow()).thenReturn(clientProfile);
        when(carePlanRepository.findByClientProfileId(any())).thenReturn(Optional.of(carePlan));
        when(goalRepository.findByIdAndCarePlanId(eq(1L), any())).thenReturn(Optional.of(goal));

        var result = goalService.getMyGoalById(1L);

        assertNotNull(result);
    }

    @Test
    void create_shouldThrowWhenEvaluationDateIsInPast() {
        GoalCreateRequest req = new GoalCreateRequest();
        req.setEvaluationDate(LocalDate.now().minusDays(1));

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> goalService.create(1L, req)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void create_shouldThrowWhenEvaluationDateIsNull() {
        GoalCreateRequest req = new GoalCreateRequest();
        req.setEvaluationDate(null);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> goalService.create(1L, req)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}