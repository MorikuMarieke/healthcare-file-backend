package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.careteam.CareTeamRequest;
import com.moriku.healthcare_file_backend.dto.careteam.CareTeamResponse;
import com.moriku.healthcare_file_backend.mapper.CareTeamMapper;
import com.moriku.healthcare_file_backend.model.CareTeam;
import com.moriku.healthcare_file_backend.repository.CareTeamRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CareTeamService {

    private final CareTeamRepository careTeamRepository;

    public CareTeamService(CareTeamRepository careTeamRepository) {
        this.careTeamRepository = careTeamRepository;
    }

    @Transactional
    public CareTeamResponse createCareTeam(CareTeamRequest request) {
        if (request.getTeamName() == null || request.getTeamName().isBlank()
            || request.getTeamPhoneNumber() == null || request.getTeamPhoneNumber().isBlank()
            || request.getTeamEmail() == null || request.getTeamEmail().isBlank()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All fields are required");
        }

        CareTeam saved = careTeamRepository.save(CareTeamMapper.toEntity(request));
        return CareTeamMapper.toResponse(saved);
    }

    public CareTeamResponse getCareTeam(Long id) {
        CareTeam team = careTeamRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CareTeam not found"));

        return CareTeamMapper.toResponse(team);
    }

    public List<CareTeamResponse> getAllCareTeams() {
        return careTeamRepository.findAll()
            .stream()
            .map(CareTeamMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public CareTeamResponse updateCareTeam(Long id, CareTeamRequest request) {
        CareTeam team = careTeamRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CareTeam not found"));

        if (request == null || request.getTeamName() == null || request.getTeamName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "teamName is required");
        }

        CareTeamMapper.updateEntity(team, request);
        return CareTeamMapper.toResponse(team);
    }

    @Transactional
    public void deleteCareTeam(Long id) {
        CareTeam team = careTeamRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CareTeam not found"));

        careTeamRepository.delete(team);
    }
}
