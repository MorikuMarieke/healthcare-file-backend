package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.ClientProfileCreateRequestDto;
import com.moriku.healthcare_file_backend.dto.ClientProfileResponseDto;
import com.moriku.healthcare_file_backend.dto.ClientProfileUpdateRequestDto;
import com.moriku.healthcare_file_backend.mapper.ClientProfileMapper;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.ContactDetails;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import com.moriku.healthcare_file_backend.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
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
    public ClientProfileResponseDto createClientProfile(ClientProfileCreateRequestDto req) {
        String bsn = req.getBsn().trim();

        if (clientProfileRepository.existsByBsn(bsn)) {
            throw new IllegalArgumentException("ClientProfile already exists for BSN: " + bsn);
        }

        ClientProfile profile = ClientProfileMapper.toEntity(req);
        ContactDetails details = new ContactDetails();
        details.setClientProfile(profile);
        profile.setContactDetails(details);
        ClientProfile saved = clientProfileRepository.save(profile);
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

    public ClientProfileResponseDto getMyClientProfile() {
        User user = getCurrentUserOrThrow();

        if (!user.isClient()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only CLIENT can access /me/client-profile");
        }

        ClientProfile profile = clientProfileRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client profile not found"));

        return ClientProfileMapper.toResponse(profile);
    }


    public List<ClientProfileResponseDto> getAllClientProfiles() {
        return clientProfileRepository.findAll().stream()
            .map(ClientProfileMapper::toResponse)
            .toList();
    }

    public ClientProfileResponseDto getClientProfileById(Long id) {
        ClientProfile profile = clientProfileRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ClientProfile not found with id: " + id));
        return ClientProfileMapper.toResponse(profile);
    }

    @Transactional
    public ClientProfileResponseDto patchClientProfile(Long id, ClientProfileUpdateRequestDto req) {
        ClientProfile profile = clientProfileRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ClientProfile not found with id: " + id));

        if (req.getFirstName() != null) profile.setFirstName(req.getFirstName().trim());
        if (req.getLastName() != null) profile.setLastName(req.getLastName().trim());
        if (req.getActive() != null) profile.setActive(req.getActive());

        return ClientProfileMapper.toResponse(profile);
    }

    public void deleteClientProfile(Long id) {
        if (!clientProfileRepository.existsById(id)) {
            throw new IllegalArgumentException("ClientProfile not found with id: " + id);
        }
        clientProfileRepository.deleteById(id);
    }

    @Transactional
    public void patchContactEmail(Long clientProfileId, String email) {
        ClientProfile profile = clientProfileRepository.findById(clientProfileId)
            .orElseThrow(() -> new IllegalArgumentException("ClientProfile not found with id: " + clientProfileId));

        ContactDetails details = profile.getContactDetails();
        if (details == null) {
            details = new ContactDetails();
            details.setClientProfile(profile);
            profile.setContactDetails(details);
        }

        details.setEmail(email.trim());
    }


}
