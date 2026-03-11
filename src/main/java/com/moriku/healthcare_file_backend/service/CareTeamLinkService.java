package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.careteam.CareTeamClientResponse;
import com.moriku.healthcare_file_backend.dto.careteam.CareTeamMemberResponse;
import com.moriku.healthcare_file_backend.mapper.CareTeamMapper;
import com.moriku.healthcare_file_backend.model.*;
import com.moriku.healthcare_file_backend.repository.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CareTeamLinkService {

    private final CareTeamRepository careTeamRepository;
    private final CareTeamMemberRepository memberRepository;
    private final CareTeamClientRepository clientRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final ClientProfileRepository clientProfileRepository;

    public CareTeamLinkService(
        CareTeamRepository careTeamRepository,
        CareTeamMemberRepository memberRepository,
        CareTeamClientRepository clientRepository,
        EmployeeProfileRepository employeeProfileRepository,
        ClientProfileRepository clientProfileRepository
    ) {
        this.careTeamRepository = careTeamRepository;
        this.memberRepository = memberRepository;
        this.clientRepository = clientRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.clientProfileRepository = clientProfileRepository;
    }

    // ===== Members =====

    @Transactional
    public CareTeamMemberResponse addMember(Long teamId, Long employeeId) {
        if (memberRepository.existsByCareTeamIdAndEmployeeProfileId(teamId, employeeId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already member");
        }

        CareTeam team = careTeamRepository.findById(teamId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CareTeam not found"));

        EmployeeProfile employee = employeeProfileRepository.findById(employeeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EmployeeProfile not found"));

        CareTeamMember saved = memberRepository.save(new CareTeamMember(team, employee));

        System.out.println("addMember called: teamId=" + teamId + ", employeeId=" + employeeId);
        return CareTeamMapper.toMemberResponse(saved);
    }

    @Transactional
    public void removeMember(Long teamId, Long employeeId) {
        memberRepository.deleteByCareTeamIdAndEmployeeProfileId(teamId, employeeId);
    }

    public List<CareTeamMemberResponse> getAllTeamEmployees(Long teamId) {
        return memberRepository.findAllByCareTeamId(teamId)
            .stream()
            .map(CareTeamMapper::toMemberResponse)
            .collect(Collectors.toList());
    }

    // ===== Clients =====

    @Transactional
    public CareTeamClientResponse addClient(Long teamId, Long clientId) {
        if (clientRepository.existsByCareTeamIdAndClientProfileId(teamId, clientId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already linked");
        }

        CareTeam team = careTeamRepository.findById(teamId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CareTeam not found"));

        ClientProfile client = clientProfileRepository.findById(clientId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClientProfile not found"));

        CareTeamClient saved = clientRepository.save(new CareTeamClient(team, client));
        return CareTeamMapper.toClientResponse(saved);
    }

    @Transactional
    public void removeClient(Long teamId, Long clientId) {
        clientRepository.deleteByCareTeamIdAndClientProfileId(teamId, clientId);
    }

    public List<CareTeamClientResponse> getAllTeamClients(Long teamId) {
        return clientRepository.findAllByCareTeamId(teamId)
            .stream()
            .map(CareTeamMapper::toClientResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public CareTeamMemberResponse moveMember(Long fromTeamId, Long employeeId, Long toTeamId) {
        if (fromTeamId.equals(toTeamId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fromTeamId and toTeamId must be different");
        }

        CareTeamMember existingLink = memberRepository.findByCareTeamIdAndEmployeeProfileId(fromTeamId, employeeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member link not found"));

        if (memberRepository.existsByCareTeamIdAndEmployeeProfileId(toTeamId, employeeId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already member of target team");
        }

        CareTeam toTeam = careTeamRepository.findById(toTeamId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target CareTeam not found"));

        EmployeeProfile employee = existingLink.getEmployeeProfile();

        memberRepository.delete(existingLink);

        CareTeamMember saved = memberRepository.save(new CareTeamMember(toTeam, employee));
        return CareTeamMapper.toMemberResponse(saved);
    }

    @Transactional
    public CareTeamClientResponse moveClient(Long fromTeamId, Long clientId, Long toTeamId) {
        if (fromTeamId.equals(toTeamId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fromTeamId and toTeamId must be different");
        }

        CareTeamClient existingLink = clientRepository.findByCareTeamIdAndClientProfileId(fromTeamId, clientId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client link not found"));

        if (clientRepository.existsByCareTeamIdAndClientProfileId(toTeamId, clientId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already client of target team");
        }

        CareTeam toTeam = careTeamRepository.findById(toTeamId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target CareTeam not found"));

        ClientProfile client = existingLink.getClientProfile();

        clientRepository.delete(existingLink);

        CareTeamClient saved = clientRepository.save(new CareTeamClient(toTeam, client));
        return CareTeamMapper.toClientResponse(saved);
    }

    // ===== Access Gate =====

    public void assertEmployeeCanAccessClient(Long employeeId, Long clientId) {
        boolean allowed = memberRepository.existsSharedTeamAccess(employeeId, clientId);
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No team access");
        }
    }
}
