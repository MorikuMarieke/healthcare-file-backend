package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileCreateRequest;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.CarePlanRepository;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientProfileServiceTest {

    @Mock
    private ClientProfileRepository clientProfileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarePlanRepository carePlanRepository;

    @Mock
    private EmployeeProfileRepository employeeProfileRepository;

    @InjectMocks
    private ClientProfileService clientProfileService;

    private ClientProfile clientProfile;
    private User user;
    private EmployeeProfile employeeProfile;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@test.com");

        employeeProfile = new EmployeeProfile();
        employeeProfile.setUser(user);

        clientProfile = new ClientProfile();
    }

    @Test
    void createClientProfile_shouldCreateProfile() {
        ClientProfileCreateRequest req = new ClientProfileCreateRequest();
        req.setBsn("123");

        when(clientProfileRepository.existsByBsn("123")).thenReturn(false);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(employeeProfileRepository.findByUser_Id(any())).thenReturn(Optional.of(employeeProfile));
        when(clientProfileRepository.save(any(ClientProfile.class))).thenReturn(clientProfile);

        var result = clientProfileService.createClientProfile(req);

        assertNotNull(result);

        verify(clientProfileRepository).save(any(ClientProfile.class));
        verify(carePlanRepository).save(any());
    }

    @Test
    void createClientProfile_shouldThrowWhenBsnExists() {
        ClientProfileCreateRequest req = new ClientProfileCreateRequest();
        req.setBsn("123");

        when(clientProfileRepository.existsByBsn("123")).thenReturn(true);

        assertThrows(
            RuntimeException.class,
            () -> clientProfileService.createClientProfile(req)
        );
    }

    @Test
    void createClientProfile_shouldThrowWhenEmployeeNotFound() {
        ClientProfileCreateRequest req = new ClientProfileCreateRequest();
        req.setBsn("123");

        when(clientProfileRepository.existsByBsn("123")).thenReturn(false);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(employeeProfileRepository.findByUser_Id(1L)).thenReturn(Optional.empty());

        assertThrows(
            RuntimeException.class,
            () -> clientProfileService.createClientProfile(req)
        );
    }
}
