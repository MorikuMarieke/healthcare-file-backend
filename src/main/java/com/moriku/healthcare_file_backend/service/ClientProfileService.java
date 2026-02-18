package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileCreateRequest;
import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileResponse;
import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileStatusRequest;
import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileUpdateRequest;
import com.moriku.healthcare_file_backend.exception.BadRequestException;
import com.moriku.healthcare_file_backend.exception.ConflictException;
import com.moriku.healthcare_file_backend.exception.ResourceNotFoundException;
import com.moriku.healthcare_file_backend.mapper.ClientProfileMapper;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.ContactDetails;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
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

    public ClientProfileService(ClientProfileRepository clientProfileRepository, UserRepository userRepository) {
        this.clientProfileRepository = clientProfileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ClientProfileResponse createClientProfile(ClientProfileCreateRequest req) {
        String bsn = req.getBsn().trim();

        if (clientProfileRepository.existsByBsn(bsn)) {
            throw new ConflictException("ClientProfile already exists for BSN: " + bsn);
        }

        ClientProfile profile = ClientProfileMapper.toEntity(req);

        ContactDetails details = new ContactDetails();
        details.setClientProfile(profile);
        profile.setContactDetails(details);

        ClientProfile saved = clientProfileRepository.save(profile);
        return ClientProfileMapper.toResponse(saved);
    }

    /**
     * TODO: Dit blijft tijdelijk ResponseStatusException (401),
     * omdat je (nog) geen UnauthorizedException hebt.
     * Als je security /me/** al op authenticated hebt staan, komt dit bijna nooit voor.
     */
    private User getCurrentUserOrThrow() {
        String email = SecurityUtils.getCurrentEmail();

        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    @Transactional(readOnly = true)
    public ClientProfileResponse getMyClientProfile() {
        User user = getCurrentUserOrThrow();

        // role check hoort idealiter in SecurityConfig (requestMatcher /me/client-profile -> CLIENT)
        ClientProfile profile = clientProfileRepository.findByUserIdAndActiveTrue(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Client profile not found"));

        return ClientProfileMapper.toResponse(profile);
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

    @Transactional
    public void patchContactEmail(Long clientProfileId, String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email must not be blank");
        }

        ClientProfile profile = clientProfileRepository.findById(clientProfileId)
            .orElseThrow(() -> new ResourceNotFoundException("ClientProfile not found with id: " + clientProfileId));

        ContactDetails details = profile.getContactDetails();
        if (details == null) {
            details = new ContactDetails();
            details.setClientProfile(profile);
            profile.setContactDetails(details);
        }

        details.setEmail(email.trim());
    }

    @Transactional
    public void setClientProfileActive(Long clientProfileId, ClientProfileStatusRequest dto) {
        ClientProfile clientProfile = clientProfileRepository.findById(clientProfileId)
            .orElseThrow(() -> new ResourceNotFoundException("ClientProfile not found with id: " + clientProfileId));

        clientProfile.setActive(Boolean.TRUE.equals(dto.getActive()));
        clientProfileRepository.save(clientProfile);
    }
}
