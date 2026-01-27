package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.ClientProfileCreateRequestDto;
import com.moriku.healthcare_file_backend.dto.ClientProfileResponseDto;
import com.moriku.healthcare_file_backend.dto.ClientProfileUpdateRequestDto;
import com.moriku.healthcare_file_backend.dto.ContactDetailsPatchRequestDto;
import com.moriku.healthcare_file_backend.service.ClientProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client-profiles")
public class ClientProfileController {

    private final ClientProfileService clientProfileService;

    public ClientProfileController(ClientProfileService clientProfileService) {
        this.clientProfileService = clientProfileService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientProfileResponseDto createClientProfile(@Valid @RequestBody ClientProfileCreateRequestDto request) {
        return clientProfileService.createClientProfile(request);
    }

    @GetMapping
    public List<ClientProfileResponseDto> getAllClientProfiles() {
        return clientProfileService.getAllClientProfiles();
    }

    @GetMapping("/{id}")
    public ClientProfileResponseDto getClientProfileById(@PathVariable Long id) {
        return clientProfileService.getClientProfileById(id);
    }

    @PatchMapping("/{id}")
    public ClientProfileResponseDto patchClientProfile(
        @PathVariable Long id,
        @RequestBody ClientProfileUpdateRequestDto request
    ) {
        return clientProfileService.patchClientProfile(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClientProfile(@PathVariable Long id) {
        clientProfileService.deleteClientProfile(id);
    }

    @PatchMapping("/{id}/contact-details")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchContactDetails(@PathVariable Long id, @Valid @RequestBody ContactDetailsPatchRequestDto req) {
        clientProfileService.patchContactEmail(id, req.getEmail());
    }

}
