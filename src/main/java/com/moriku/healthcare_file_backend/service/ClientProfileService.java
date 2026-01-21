package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.ClientProfileCreateRequestDto;
import com.moriku.healthcare_file_backend.dto.ClientProfileResponseDto;
import com.moriku.healthcare_file_backend.dto.ClientProfileUpdateRequestDto;
import com.moriku.healthcare_file_backend.mapper.ClientProfileMapper;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.ContactDetails;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;

    public ClientProfileService(ClientProfileRepository clientProfileRepository) {
        this.clientProfileRepository = clientProfileRepository;
    }

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

    public ClientProfileResponseDto patchClientProfile(Long id, ClientProfileUpdateRequestDto req) {
        ClientProfile profile = clientProfileRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ClientProfile not found with id: " + id));

        if (req.getFirstName() != null) profile.setFirstName(req.getFirstName().trim());
        if (req.getLastName() != null) profile.setLastName(req.getLastName().trim());
        if (req.getActive() != null) profile.setActive(req.getActive());

        ClientProfile saved = clientProfileRepository.save(profile);
        return ClientProfileMapper.toResponse(saved);
    }

    public void deleteClientProfile(Long id) {
        if (!clientProfileRepository.existsById(id)) {
            throw new IllegalArgumentException("ClientProfile not found with id: " + id);
        }
        clientProfileRepository.deleteById(id);
    }

    public void patchContactEmail(Long clientProfileId, String email) {
        ClientProfile profile = clientProfileRepository.findById(clientProfileId)
            .orElseThrow(() -> new IllegalArgumentException("ClientProfile not found with id: " + clientProfileId));

        ContactDetails details = profile.getContactDetails();
        if (details == null) { // defensive
            details = new ContactDetails();
            details.setClientProfile(profile);
            profile.setContactDetails(details);
        }

        details.setEmail(email.trim());
        clientProfileRepository.save(profile);
    }

}
