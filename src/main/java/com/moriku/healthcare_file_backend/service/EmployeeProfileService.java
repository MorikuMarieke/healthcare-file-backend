package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.employee_profile.EmployeeProfileResponse;
import com.moriku.healthcare_file_backend.dto.employee_profile.EmployeeProfileUpdateRequest;
import com.moriku.healthcare_file_backend.exception.ResourceNotFoundException;
import com.moriku.healthcare_file_backend.mapper.EmployeeProfileMapper;
import com.moriku.healthcare_file_backend.model.CareTeam;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.repository.CareTeamRepository;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmployeeProfileService {

    private final EmployeeProfileRepository employeeProfileRepository;
    private final CareTeamRepository careTeamRepository;

    public EmployeeProfileService(EmployeeProfileRepository employeeProfileRepository,
                                  CareTeamRepository careTeamRepository) {
        this.employeeProfileRepository = employeeProfileRepository;
        this.careTeamRepository = careTeamRepository;
    }

    private EmployeeProfile getEmployeeProfileOrThrow(Long userId) {
        return employeeProfileRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Employee profile does not exist for this userId: " + userId
            ));
    }

    public EmployeeProfileResponse getEmployeeProfile(Long userId) {
        EmployeeProfile profile = getEmployeeProfileOrThrow(userId);
        return EmployeeProfileMapper.toResponse(profile);
    }

    @Transactional
    public EmployeeProfileResponse patchEmployeeProfile(Long userId, EmployeeProfileUpdateRequest req) {
        EmployeeProfile profile = getEmployeeProfileOrThrow(userId);

        if (req.getWorkPhoneNumber() != null) profile.setWorkPhoneNumber(req.getWorkPhoneNumber().trim());
        if (req.getPersonalPhoneNumber() != null) profile.setPersonalPhoneNumber(req.getPersonalPhoneNumber().trim());
        if (req.getPersonalEmail() != null) profile.setPersonalEmail(req.getPersonalEmail().trim().toLowerCase());

        return EmployeeProfileMapper.toResponse(profile);
    }

    @Transactional
    public EmployeeProfileResponse assignEmployeeToTeam(Long employeeId, Long teamId) {
        EmployeeProfile employee = employeeProfileRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("EmployeeProfile not found"));

        CareTeam team = careTeamRepository.findById(teamId)
            .orElseThrow(() -> new ResourceNotFoundException("CareTeam not found"));

        if (employee.getCareTeam() != null &&
            employee.getCareTeam().getId().equals(team.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee already assigned to this team");
        }

        employee.setCareTeam(team);

        return EmployeeProfileMapper.toResponse(employee);
    }

}
