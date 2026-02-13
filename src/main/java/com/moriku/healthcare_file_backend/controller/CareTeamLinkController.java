package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.careteam.CareTeamClientResponse;
import com.moriku.healthcare_file_backend.dto.careteam.CareTeamMemberResponse;
import com.moriku.healthcare_file_backend.service.CareTeamLinkService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/care-teams")
public class CareTeamLinkController {

    private final CareTeamLinkService service;

    public CareTeamLinkController(CareTeamLinkService service) {
        this.service = service;
    }

    @PostMapping("/{teamId}/employees/{employeeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CareTeamMemberResponse addMember(@PathVariable Long teamId, @PathVariable Long employeeId) {
        return service.addMember(teamId, employeeId);
    }

    @DeleteMapping("/{teamId}/employees/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(@PathVariable Long teamId, @PathVariable Long employeeId) {
        service.removeMember(teamId, employeeId);
    }

    @GetMapping("/{teamId}/employees")
    public List<CareTeamMemberResponse> getEmployees(@PathVariable Long teamId) {
        return service.getAllTeamEmployees(teamId);
    }

    @PostMapping("/{teamId}/clients/{clientId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CareTeamClientResponse addClient(@PathVariable Long teamId, @PathVariable Long clientId) {
        return service.addClient(teamId, clientId);
    }

    @DeleteMapping("/{teamId}/clients/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeClient(@PathVariable Long teamId, @PathVariable Long clientId) {
        service.removeClient(teamId, clientId);
    }

    @GetMapping("/{teamId}/clients")
    public List<CareTeamClientResponse> getClients(@PathVariable Long teamId) {
        return service.getAllTeamClients(teamId);
    }
}
