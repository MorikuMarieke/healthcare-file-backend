package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.careteam.CareTeamRequest;
import com.moriku.healthcare_file_backend.dto.careteam.CareTeamResponse;
import com.moriku.healthcare_file_backend.service.CareTeamService;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/care-teams")
public class CareTeamController {

    private final CareTeamService careTeamService;

    public CareTeamController(CareTeamService careTeamService) {
        this.careTeamService = careTeamService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CareTeamResponse create(@RequestBody @Valid CareTeamRequest req) {
        return careTeamService.createCareTeam(req);
    }

    @GetMapping("/{id}")
    public CareTeamResponse getById(@PathVariable Long id) {
        return careTeamService.getCareTeam(id);
    }

    @GetMapping
    public List<CareTeamResponse> getAll() {
        return careTeamService.getAllCareTeams();
    }

    @PutMapping("/{id}")
    public CareTeamResponse update(@PathVariable Long id, @RequestBody CareTeamRequest request) {
        return careTeamService.updateCareTeam(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        careTeamService.deleteCareTeam(id);
    }
}
