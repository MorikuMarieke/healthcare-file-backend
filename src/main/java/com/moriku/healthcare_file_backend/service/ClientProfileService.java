package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileCreateRequest;
import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileResponse;
import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileStatusRequest;
import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileUpdateRequest;
import com.moriku.healthcare_file_backend.dto.client_profile.ContactDetailsPatchRequest;
import com.moriku.healthcare_file_backend.dto.client_profile.ContactDetailsResponse;
import com.moriku.healthcare_file_backend.exception.ConflictException;
import com.moriku.healthcare_file_backend.exception.ResourceNotFoundException;
import com.moriku.healthcare_file_backend.mapper.ClientProfileMapper;
import com.moriku.healthcare_file_backend.mapper.ContactDetailsMapper;
import com.moriku.healthcare_file_backend.model.CarePlan;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.ContactDetails;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.CarePlanRepository;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import com.moriku.healthcare_file_backend.security.SecurityContextService;
import com.moriku.healthcare_file_backend.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;
    private final UserRepository userRepository;
    private final CarePlanRepository carePlanRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final SecurityContextService securityContextService;

    public ClientProfileService(
        ClientProfileRepository clientProfileRepository,
        UserRepository userRepository,
        CarePlanRepository carePlanRepository,
        EmployeeProfileRepository employeeProfileRepository,
        SecurityContextService securityContextService
    ) {
        this.clientProfileRepository = clientProfileRepository;
        this.userRepository = userRepository;
        this.carePlanRepository = carePlanRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.securityContextService = securityContextService;
    }

    @Transactional
    public ClientProfileResponse createClientProfile(ClientProfileCreateRequest req) {
        String bsn = req.getBsn().trim();

        if (clientProfileRepository.existsByBsn(bsn)) {
            throw new ConflictException("ClientProfile already exists for BSN: " + bsn);
        }

        User currentUser = getCurrentUserOrThrow();

        EmployeeProfile employeeProfile = employeeProfileRepository.findByUser_Id(currentUser.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found for current user"));

        if (employeeProfile.getCareTeam() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current employee has no care team");
        }

        ClientProfile profile = ClientProfileMapper.toEntity(req);
        profile.setCareTeam(employeeProfile.getCareTeam());

        ContactDetails details = new ContactDetails();
        details.setClientProfile(profile);
        profile.setContactDetails(details);

        ClientProfile saved = clientProfileRepository.save(profile);

        CarePlan plan = new CarePlan();
        plan.setClientProfile(saved);
        plan.setNotes("");
        plan.setMedicalHistory("");
        carePlanRepository.save(plan);

        return ClientProfileMapper.toResponse(saved);
    }

    private User getCurrentUserOrThrow() {
        String email = SecurityUtils.getCurrentEmail();

        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private ClientProfile getMyActiveClientProfileOrThrow() {
        User user = getCurrentUserOrThrow();

        return clientProfileRepository.findByUserIdAndActiveTrue(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Client profile not found"));
    }

    @Transactional(readOnly = true)
    public ClientProfileResponse getMyClientProfile() {
        ClientProfile profile = getMyActiveClientProfileOrThrow();
        return ClientProfileMapper.toResponse(profile);
    }

    @Transactional(readOnly = true)
    public ContactDetailsResponse getMyContactDetails() {
        ClientProfile profile = getMyActiveClientProfileOrThrow();

        ContactDetails details = profile.getContactDetails();
        if (details == null) {
            details = new ContactDetails();
            details.setClientProfile(profile);
            profile.setContactDetails(details);
        }

        return ContactDetailsMapper.toResponse(details);
    }

    @Transactional(readOnly = true)
    public List<ClientProfileResponse> getAllClientProfiles() {
        return clientProfileRepository.findAll().stream()
            .map(ClientProfileMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public ClientProfileResponse getClientProfileById(Long id) {
        ClientProfile profile = clientProfileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ClientProfile not found with id: " + id));
        return ClientProfileMapper.toResponse(profile);
    }

    @Transactional
    public ClientProfileResponse patchClientProfile(Long id, ClientProfileUpdateRequest req) {
        ClientProfile profile = clientProfileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ClientProfile not found with id: " + id));

        securityContextService.assertCurrentEmployeeHasAccessToClientForWriteOrThrow(profile);

        if (req.getFirstName() != null && !req.getFirstName().isBlank()) {
            profile.setFirstName(req.getFirstName().trim());
        }

        if (req.getLastName() != null && !req.getLastName().isBlank()) {
            profile.setLastName(req.getLastName().trim());
        }

        if (req.getSex() != null) {
            profile.setSex(req.getSex());
        }

        if (req.getBirthDate() != null) {
            profile.setBirthDate(req.getBirthDate());
        }

        return ClientProfileMapper.toResponse(profile);
    }

    @Transactional
    public void deleteClientProfile(Long id) {
        ClientProfile profile = clientProfileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ClientProfile not found with id: " + id));
        clientProfileRepository.delete(profile);
    }

    @Transactional(readOnly = true)
    public ContactDetailsResponse getContactDetailsByClientProfileId(Long clientProfileId) {
        ClientProfile profile = clientProfileRepository.findById(clientProfileId)
            .orElseThrow(() -> new ResourceNotFoundException("ClientProfile not found with id: " + clientProfileId));

        ContactDetails details = profile.getContactDetails();
        if (details == null) {
            details = new ContactDetails();
            details.setClientProfile(profile);
            profile.setContactDetails(details);
        }

        return ContactDetailsMapper.toResponse(details);
    }

    @Transactional
    public ContactDetailsResponse patchContactDetails(Long clientProfileId, ContactDetailsPatchRequest req) {
        ClientProfile profile = clientProfileRepository.findById(clientProfileId)
            .orElseThrow(() -> new ResourceNotFoundException("ClientProfile not found with id: " + clientProfileId));

        securityContextService.assertCurrentEmployeeHasAccessToClientForWriteOrThrow(profile);

        ContactDetails details = profile.getContactDetails();
        if (details == null) {
            details = new ContactDetails();
            details.setClientProfile(profile);
            profile.setContactDetails(details);
        }

        if (req.getEmail() != null) {
            details.setEmail(req.getEmail().trim().toLowerCase());
        }

        if (req.getPrimaryPhoneNumber() != null) {
            details.setPrimaryPhoneNumber(req.getPrimaryPhoneNumber().trim());
        }

        if (req.getSecondaryPhoneNumber() != null) {
            details.setSecondaryPhoneNumber(req.getSecondaryPhoneNumber().trim());
        }

        if (req.getPrimaryPhysician() != null) {
            details.setPrimaryPhysician(req.getPrimaryPhysician().trim());
        }

        if (req.getFirstEmergencyContactName() != null) {
            details.setFirstEmergencyContactName(req.getFirstEmergencyContactName().trim());
        }

        if (req.getFirstEmergencyContactEmail() != null) {
            details.setFirstEmergencyContactEmail(req.getFirstEmergencyContactEmail().trim().toLowerCase());
        }

        if (req.getFirstEmergencyContactPhoneNumber() != null) {
            details.setFirstEmergencyContactPhoneNumber(req.getFirstEmergencyContactPhoneNumber().trim());
        }

        if (req.getAddress() != null) {
            details.setAddress(req.getAddress().trim());
        }

        return ContactDetailsMapper.toResponse(details);
    }

    @Transactional
    public void setClientProfileActive(Long clientProfileId, ClientProfileStatusRequest dto) {
        ClientProfile profile = clientProfileRepository.findById(clientProfileId)
            .orElseThrow(() -> new ResourceNotFoundException("ClientProfile not found with id: " + clientProfileId));

        securityContextService.assertCurrentEmployeeHasAccessToClientForWriteOrThrow(profile);

        profile.setActive(Boolean.TRUE.equals(dto.getActive()));
        clientProfileRepository.save(profile);
    }
}