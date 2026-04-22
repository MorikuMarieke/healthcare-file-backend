package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.client_profile.*;
import com.moriku.healthcare_file_backend.exception.ConflictException;
import com.moriku.healthcare_file_backend.exception.ResourceNotFoundException;
import com.moriku.healthcare_file_backend.model.*;
import com.moriku.healthcare_file_backend.repository.*;
import com.moriku.healthcare_file_backend.security.SecurityContextService;
import com.moriku.healthcare_file_backend.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Mock
    private SecurityContextService securityContextService;

    @InjectMocks
    private ClientProfileService clientProfileService;

    @Mock
    private CareTeamRepository careTeamRepository;

    private ClientProfile clientProfile;
    private User user;
    private EmployeeProfile employeeProfile;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@test.com");

        employeeProfile = new EmployeeProfile();
        employeeProfile.setUser(user);

        CareTeam careTeam = new CareTeam(
            "Test Team",
            "0612345678",
            "team@test.local"
        );
        employeeProfile.setCareTeam(careTeam);

        clientProfile = new ClientProfile();
    }

    @Test
    void createClientProfile_shouldCreateProfile() {
        ClientProfileCreateRequest req = new ClientProfileCreateRequest();
        req.setBsn("123456789");
        req.setFirstName("Test");
        req.setLastName("Client");
        req.setSex(Sex.FEMALE);
        req.setBirthDate(LocalDate.of(1999, 1, 1));

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentEmail).thenReturn("test@test.com");

            when(clientProfileRepository.existsByBsn("123456789")).thenReturn(false);
            when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
            when(employeeProfileRepository.findByUser_Id(any())).thenReturn(Optional.of(employeeProfile));
            when(clientProfileRepository.save(any(ClientProfile.class))).thenReturn(clientProfile);
            when(carePlanRepository.save(any(CarePlan.class))).thenReturn(new CarePlan());

            ClientProfileResponse result = clientProfileService.createClientProfile(req);

            assertNotNull(result);

            verify(clientProfileRepository).save(any(ClientProfile.class));
            verify(carePlanRepository).save(any(CarePlan.class));
        }
    }

    @Test
    void createClientProfile_shouldThrowWhenBsnAlreadyExists() {
        ClientProfileCreateRequest req = new ClientProfileCreateRequest();
        req.setBsn("123456789");

        when(clientProfileRepository.existsByBsn("123456789")).thenReturn(true);

        assertThrows(
            ConflictException.class,
            () -> clientProfileService.createClientProfile(req)
        );
    }

    @Test
    void createClientProfile_shouldThrowWhenEmployeeHasNoCareTeam() {
        ClientProfileCreateRequest req = new ClientProfileCreateRequest();
        req.setBsn("123456789");
        req.setFirstName("Test");
        req.setLastName("Client");
        req.setSex(Sex.FEMALE);
        req.setBirthDate(LocalDate.of(1999, 1, 1));

        employeeProfile.setCareTeam(null);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentEmail).thenReturn("test@test.com");

            when(clientProfileRepository.existsByBsn("123456789")).thenReturn(false);
            when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
            when(employeeProfileRepository.findByUser_Id(any())).thenReturn(Optional.of(employeeProfile));

            assertThrows(
                ResponseStatusException.class,
                () -> clientProfileService.createClientProfile(req)
            );
        }
    }

    @Test
    void patchClientProfile_shouldThrowWhenNotFound() {
        ClientProfileUpdateRequest req = new ClientProfileUpdateRequest();
        req.setFirstName("NewFirst");

        when(clientProfileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> clientProfileService.patchClientProfile(99L, req)
        );

        verify(clientProfileRepository).findById(99L);
    }

    @Test
    void patchContactDetails_shouldUpdateFields() {
        ContactDetailsPatchRequest req = new ContactDetailsPatchRequest();
        req.setEmail("NEW@TEST.COM");
        req.setPrimaryPhoneNumber("0612345678");
        req.setSecondaryPhoneNumber("0687654321");
        req.setPrimaryPhysician("Dr. Janssen");
        req.setFirstEmergencyContactName("Emergency Contact");
        req.setFirstEmergencyContactEmail("EMERGENCY@TEST.COM");
        req.setFirstEmergencyContactPhoneNumber("0699999999");
        req.setAddress("Test Street 1");

        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setClientProfile(clientProfile);
        clientProfile.setContactDetails(contactDetails);

        when(clientProfileRepository.findById(1L)).thenReturn(Optional.of(clientProfile));
        doNothing().when(securityContextService)
            .assertCurrentEmployeeHasAccessToClientForWriteOrThrow(clientProfile);

        ContactDetailsResponse result = clientProfileService.patchContactDetails(1L, req);

        assertNotNull(result);
        assertEquals("new@test.com", contactDetails.getEmail());
        assertEquals("0612345678", contactDetails.getPrimaryPhoneNumber());
        assertEquals("0687654321", contactDetails.getSecondaryPhoneNumber());
        assertEquals("Dr. Janssen", contactDetails.getPrimaryPhysician());
        assertEquals("Emergency Contact", contactDetails.getFirstEmergencyContactName());
        assertEquals("emergency@test.com", contactDetails.getFirstEmergencyContactEmail());
        assertEquals("0699999999", contactDetails.getFirstEmergencyContactPhoneNumber());
        assertEquals("Test Street 1", contactDetails.getAddress());

        verify(clientProfileRepository).findById(1L);
        verify(securityContextService).assertCurrentEmployeeHasAccessToClientForWriteOrThrow(clientProfile);
    }

    @Test
    void setClientProfileActive_shouldUpdateStatus() {
        ClientProfileStatusRequest req = new ClientProfileStatusRequest();
        req.setActive(true);

        when(clientProfileRepository.findById(1L)).thenReturn(Optional.of(clientProfile));
        doNothing().when(securityContextService)
            .assertCurrentEmployeeHasAccessToClientForWriteOrThrow(clientProfile);

        clientProfileService.setClientProfileActive(1L, req);

        assertTrue(clientProfile.isActive());

        verify(clientProfileRepository).findById(1L);
        verify(clientProfileRepository).save(clientProfile);
        verify(securityContextService).assertCurrentEmployeeHasAccessToClientForWriteOrThrow(clientProfile);
    }

    @Test
    void getClientProfileById_shouldReturnProfile() {
        when(clientProfileRepository.findById(1L)).thenReturn(Optional.of(clientProfile));

        ClientProfileResponse result = clientProfileService.getClientProfileById(1L);

        assertNotNull(result);
        verify(clientProfileRepository).findById(1L);
    }

    @Test
    void getClientProfileById_shouldThrowWhenNotFound() {
        when(clientProfileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> clientProfileService.getClientProfileById(99L)
        );

        verify(clientProfileRepository).findById(99L);
    }

    @Test
    void patchClientProfile_shouldUpdateFields() {
        ClientProfileUpdateRequest req = new ClientProfileUpdateRequest();
        req.setFirstName("NewFirst");
        req.setLastName("NewLast");
        req.setBirthDate(LocalDate.of(2000, 1, 1));
        req.setSex(Sex.FEMALE);

        when(clientProfileRepository.findById(1L)).thenReturn(Optional.of(clientProfile));
        doNothing().when(securityContextService)
            .assertCurrentEmployeeHasAccessToClientForWriteOrThrow(clientProfile);

        ClientProfileResponse result = clientProfileService.patchClientProfile(1L, req);

        assertNotNull(result);
        assertEquals("NewFirst", clientProfile.getFirstName());
        assertEquals("NewLast", clientProfile.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), clientProfile.getBirthDate());
        assertEquals(Sex.FEMALE, clientProfile.getSex());

        verify(clientProfileRepository).findById(1L);
        verify(securityContextService).assertCurrentEmployeeHasAccessToClientForWriteOrThrow(clientProfile);
    }

    @Test
    void getAllClientProfiles_shouldReturnProfiles() {
        when(clientProfileRepository.findAll()).thenReturn(List.of(clientProfile));

        List<ClientProfileResponse> result = clientProfileService.getAllClientProfiles();

        assertEquals(1, result.size());
        verify(clientProfileRepository).findAll();
    }

    @Test
    void getContactDetailsByClientProfileId_shouldReturnContactDetails() {
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setClientProfile(clientProfile);
        clientProfile.setContactDetails(contactDetails);

        when(clientProfileRepository.findById(1L)).thenReturn(Optional.of(clientProfile));

        ContactDetailsResponse result = clientProfileService.getContactDetailsByClientProfileId(1L);

        assertNotNull(result);
        verify(clientProfileRepository).findById(1L);
    }

    @Test
    void getContactDetailsByClientProfileId_shouldThrowWhenProfileNotFound() {
        when(clientProfileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> clientProfileService.getContactDetailsByClientProfileId(99L)
        );

        verify(clientProfileRepository).findById(99L);
    }

    @Test
    void deleteClientProfile_shouldDeleteProfile() {
        when(clientProfileRepository.findById(1L)).thenReturn(Optional.of(clientProfile));

        clientProfileService.deleteClientProfile(1L);

        verify(clientProfileRepository).findById(1L);
        verify(clientProfileRepository).delete(clientProfile);
    }

    @Test
    void getMyClientProfile_shouldReturnProfile() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentEmail).thenReturn("test@test.com");

            when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
            when(clientProfileRepository.findByUserIdAndActiveTrue(any()))
                .thenReturn(Optional.of(clientProfile));

            ClientProfileResponse result = clientProfileService.getMyClientProfile();

            assertNotNull(result);
        }
    }

    @Test
    void getMyClientProfile_shouldThrowWhenNotFound() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentEmail).thenReturn("test@test.com");

            when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
            when(clientProfileRepository.findByUserIdAndActiveTrue(any()))
                .thenReturn(Optional.empty());

            assertThrows(
                ResourceNotFoundException.class,
                () -> clientProfileService.getMyClientProfile()
            );
        }
    }

    @Test
    void getContactDetailsByClientProfileId_shouldCreateWhenNull() {
        clientProfile.setContactDetails(null);

        when(clientProfileRepository.findById(1L)).thenReturn(Optional.of(clientProfile));

        ContactDetailsResponse result =
            clientProfileService.getContactDetailsByClientProfileId(1L);

        assertNotNull(result);
        assertNotNull(clientProfile.getContactDetails());
    }

    @Test
    void setClientProfileActive_shouldThrowWhenNotFound() {
        ClientProfileStatusRequest req = new ClientProfileStatusRequest();
        req.setActive(true);

        when(clientProfileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> clientProfileService.setClientProfileActive(99L, req)
        );
    }

    @Test
    void getMyContactDetails_shouldCreateContactDetailsWhenNull() {
        clientProfile.setContactDetails(null);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentEmail).thenReturn("test@test.com");

            when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
            when(clientProfileRepository.findByUserIdAndActiveTrue(any()))
                .thenReturn(Optional.of(clientProfile));

            ContactDetailsResponse result = clientProfileService.getMyContactDetails();

            assertNotNull(result);
            assertNotNull(clientProfile.getContactDetails());
            assertEquals(clientProfile, clientProfile.getContactDetails().getClientProfile());
        }
    }

    @Test
    void patchContactDetails_shouldCreateContactDetailsWhenNull() {
        ContactDetailsPatchRequest req = new ContactDetailsPatchRequest();
        req.setEmail("new@test.com");

        clientProfile.setContactDetails(null);

        when(clientProfileRepository.findById(1L)).thenReturn(Optional.of(clientProfile));
        doNothing().when(securityContextService)
            .assertCurrentEmployeeHasAccessToClientForWriteOrThrow(clientProfile);

        ContactDetailsResponse result = clientProfileService.patchContactDetails(1L, req);

        assertNotNull(result);
        assertNotNull(clientProfile.getContactDetails());
        assertEquals(clientProfile, clientProfile.getContactDetails().getClientProfile());
        assertEquals("new@test.com", clientProfile.getContactDetails().getEmail());
    }

    @Test
    void getMyClientProfile_shouldThrowWhenNotAuthenticated() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentEmail).thenReturn(null);

            assertThrows(
                ResponseStatusException.class,
                () -> clientProfileService.getMyClientProfile()
            );
        }
    }

    @Test
    void transferClientToTeam_shouldTransferWhenCurrentUserIsAdmin() {
        CareTeam currentTeam = new CareTeam("Team A", "0611111111", "a@test.local");
        CareTeam targetTeam = new CareTeam("Team B", "0622222222", "b@test.local");

        clientProfile.setCareTeam(currentTeam);

        User adminUser = new User();
        Role adminRole = new Role("ADMIN");
        adminUser.setRole(adminRole);

        when(clientProfileRepository.findById(1L)).thenReturn(Optional.of(clientProfile));
        when(careTeamRepository.findById(2L)).thenReturn(Optional.of(targetTeam));
        when(securityContextService.getCurrentUserOrThrow()).thenReturn(adminUser);

        ClientProfileResponse result = clientProfileService.transferClientToTeam(1L, 2L);

        assertNotNull(result);
        assertEquals(targetTeam, clientProfile.getCareTeam());

        verify(clientProfileRepository).findById(1L);
        verify(careTeamRepository).findById(2L);
        verify(securityContextService).getCurrentUserOrThrow();
        verify(securityContextService, never()).assertCurrentEmployeeHasAccessToClientForWriteOrThrow(any());
    }

    @Test
    void transferClientToTeam_shouldTransferWhenEmployeeHasWriteAccess() {
        CareTeam currentTeam = new CareTeam("Team A", "0611111111", "a@test.local");
        CareTeam targetTeam = new CareTeam("Team B", "0622222222", "b@test.local");

        clientProfile.setCareTeam(currentTeam);

        User employeeUser = new User();
        Role employeeRole = new Role("EMPLOYEE");
        employeeUser.setRole(employeeRole);

        when(clientProfileRepository.findById(1L)).thenReturn(Optional.of(clientProfile));
        when(careTeamRepository.findById(2L)).thenReturn(Optional.of(targetTeam));
        when(securityContextService.getCurrentUserOrThrow()).thenReturn(employeeUser);
        doNothing().when(securityContextService)
            .assertCurrentEmployeeHasAccessToClientForWriteOrThrow(clientProfile);

        ClientProfileResponse result = clientProfileService.transferClientToTeam(1L, 2L);

        assertNotNull(result);
        assertEquals(targetTeam, clientProfile.getCareTeam());

        verify(securityContextService).assertCurrentEmployeeHasAccessToClientForWriteOrThrow(clientProfile);
    }

    @Test
    void transferClientToTeam_shouldThrowWhenClientNotFound() {
        when(clientProfileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> clientProfileService.transferClientToTeam(99L, 2L)
        );

        verify(clientProfileRepository).findById(99L);
        verify(careTeamRepository, never()).findById(any());
    }

    @Test
    void transferClientToTeam_shouldThrowWhenTargetTeamNotFound() {
        when(clientProfileRepository.findById(1L)).thenReturn(Optional.of(clientProfile));
        when(careTeamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> clientProfileService.transferClientToTeam(1L, 99L)
        );

        verify(clientProfileRepository).findById(1L);
        verify(careTeamRepository).findById(99L);
    }
}