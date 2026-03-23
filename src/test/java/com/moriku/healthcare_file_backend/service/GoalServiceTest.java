package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.goal.GoalCreateRequest;
import com.moriku.healthcare_file_backend.dto.goal.GoalUpdateRequest;
import com.moriku.healthcare_file_backend.model.CarePlan;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.Goal;
import com.moriku.healthcare_file_backend.repository.CarePlanRepository;
import com.moriku.healthcare_file_backend.repository.GoalRepository;
import com.moriku.healthcare_file_backend.security.SecurityContextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private CarePlanRepository carePlanRepository;

    @Mock
    private MeService meService;

    @Mock
    private SecurityContextService securityContextService;

    @InjectMocks
    private GoalService goalService;

    private ClientProfile clientProfile;
    private CarePlan carePlan;
    private Goal goal;

    @BeforeEach
    void setUp() {
        clientProfile = new ClientProfile();
        clientProfile.setCareTeam(null); // later aanpassen als nodig

        carePlan = new CarePlan();
        carePlan.setClientProfile(clientProfile);

        goal = new Goal();
        goal.setCarePlan(carePlan);
    }

    @Test
    void getAll_shouldReturnGoalResponses() {
        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.of(carePlan));
        when(goalRepository.findAllByCarePlanId(carePlan.getId())).thenReturn(List.of(goal));

        List<?> result = goalService.getAll(1L);

        assertEquals(1, result.size());
        verify(carePlanRepository).findByClientProfileId(1L);
        verify(goalRepository).findAllByCarePlanId(carePlan.getId());
    }

    @Test
    void getOne_shouldReturnGoal() {
        // Arrange
        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.of(carePlan));
        when(goalRepository.findByIdAndCarePlanId(1L, carePlan.getId()))
            .thenReturn(Optional.of(goal));

        // Act
        var result = goalService.getOne(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(goalRepository).findByIdAndCarePlanId(1L, carePlan.getId());
    }

    @Test
    void getAll_shouldThrowWhenCarePlanNotFound() {
        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
            () -> goalService.getAll(1L));
    }

    @Test
    void create_shouldCreateGoal() {
        // Arrange
        GoalCreateRequest req = new GoalCreateRequest();
        req.setEvaluationDate(LocalDate.now().plusDays(1)); // geldig (future)

        when(carePlanRepository.findByClientProfileId(1L))
            .thenReturn(Optional.of(carePlan));

        when(meService.getMyEmployeeId())
            .thenReturn(1L);

        when(carePlanRepository.save(any(CarePlan.class)))
            .thenReturn(carePlan);

        // Act
        var result = goalService.create(1L, req);

        // Assert
        assertNotNull(result);

        verify(carePlanRepository).findByClientProfileId(1L);
        verify(carePlanRepository).save(carePlan);
    }

    @Test
    void getAll_shouldReturnEmptyListWhenNoGoalsExist() {
        // Arrange
        when(carePlanRepository.findByClientProfileId(1L)).thenReturn(Optional.of(carePlan));
        when(goalRepository.findAllByCarePlanId(carePlan.getId())).thenReturn(List.of());

        // Act
        var result = goalService.getAll(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(carePlanRepository).findByClientProfileId(1L);
        verify(goalRepository).findAllByCarePlanId(carePlan.getId());
    }

    @Test
    void create_shouldThrowWhenEvaluationDateIsToday() {
        // Arrange
        GoalCreateRequest req = new GoalCreateRequest();
        req.setEvaluationDate(LocalDate.now());

        // Act + Assert
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> goalService.create(1L, req)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("evaluationDate must be in the future"));

        verify(carePlanRepository, never()).findByClientProfileId(anyLong());
        verify(carePlanRepository, never()).save(any(CarePlan.class));
    }
}