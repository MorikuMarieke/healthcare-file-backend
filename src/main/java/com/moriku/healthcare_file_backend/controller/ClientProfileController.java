package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.client_profile.*;
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
    public ClientProfileResponse createClientProfile(@Valid @RequestBody ClientProfileCreateRequest request) {
        return clientProfileService.createClientProfile(request);
    }

    @GetMapping
    public List<ClientProfileResponse> getAllClientProfiles() {
        return clientProfileService.getAllClientProfiles();
    }

    @GetMapping("/{id}")
    public ClientProfileResponse getClientProfileById(@PathVariable Long id) {
        return clientProfileService.getClientProfileById(id);
    }

    @PatchMapping("/{id}")
    public ClientProfileResponse patchClientProfile(
        @PathVariable Long id,
        @RequestBody ClientProfileUpdateRequest request
    ) {
        return clientProfileService.patchClientProfile(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClientProfile(@PathVariable Long id) {
        clientProfileService.deleteClientProfile(id);
    }

    @GetMapping("/{id}/contact-details")
    public ContactDetailsResponse getContactDetails(@PathVariable Long id) {
        return clientProfileService.getContactDetailsByClientProfileId(id);
    }

    @PatchMapping("/{id}/contact-details")
    public ContactDetailsResponse patchContactDetails(
        @PathVariable Long id,
        @Valid @RequestBody ContactDetailsPatchRequest req
    ) {
        return clientProfileService.patchContactDetails(id, req);
    }

    @PatchMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setClientProfileActive(@PathVariable Long id,
                                       @Valid @RequestBody ClientProfileStatusRequest request) {
        clientProfileService.setClientProfileActive(id, request);
    }

}
