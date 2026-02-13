package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.assignment.ClientAssignmentResponse;
import com.moriku.healthcare_file_backend.mapper.ClientAssignmentMapper;
import com.moriku.healthcare_file_backend.model.ClientAssignment;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.repository.ClientAssignmentRepository;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClientAssignmentService {

    private final ClientAssignmentRepository clientAssignmentRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final ClientProfileRepository clientProfileRepository;

    public ClientAssignmentService(
        ClientAssignmentRepository clientAssignmentRepository,
        EmployeeProfileRepository employeeProfileRepository,
        ClientProfileRepository clientProfileRepository
    ) {
        this.clientAssignmentRepository = clientAssignmentRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.clientProfileRepository = clientProfileRepository;
    }

    @Transactional
    public ClientAssignmentResponse assignClientToEmployee(Long employeeProfileId, Long clientProfileId) {
        boolean exists = clientAssignmentRepository.existsByEmployeeProfileIdAndClientProfileId(employeeProfileId, clientProfileId);
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Client is already assigned to this employee");
        }

        EmployeeProfile employeeProfile = employeeProfileRepository.findById(employeeProfileId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EmployeeProfile not found"));

        ClientProfile clientProfile = clientProfileRepository.findById(clientProfileId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClientProfile not found"));

        ClientAssignment assignment = new ClientAssignment(employeeProfile, clientProfile, LocalDateTime.now());
        ClientAssignment saved = clientAssignmentRepository.save(assignment);

        return ClientAssignmentMapper.toResponse(saved);
    }

    @Transactional
    public void unassignClientFromEmployee(Long employeeProfileId, Long clientProfileId) {
        ClientAssignment assignment = clientAssignmentRepository
            .findByEmployeeProfileIdAndClientProfileId(employeeProfileId, clientProfileId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found"));

        clientAssignmentRepository.delete(assignment);
    }

    public void assertEmployeeAssignedToClient(Long employeeProfileId, Long clientProfileId) {
        boolean allowed = clientAssignmentRepository.existsByEmployeeProfileIdAndClientProfileId(employeeProfileId, clientProfileId);
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Employee is not assigned to this client");
        }
    }

    public List<ClientAssignmentResponse> getAssignmentsForEmployee(Long employeeProfileId) {
        return clientAssignmentRepository.findAllByEmployeeProfileId(employeeProfileId)
            .stream()
            .map(ClientAssignmentMapper::toResponse)
            .collect(Collectors.toList());
    }

    public List<ClientAssignmentResponse> getAssignmentsForClient(Long clientProfileId) {
        return clientAssignmentRepository.findAllByClientProfileId(clientProfileId)
            .stream()
            .map(ClientAssignmentMapper::toResponse)
            .collect(Collectors.toList());
    }
}
