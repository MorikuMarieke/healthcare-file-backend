package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.EmployeeProfileResponseDto;
import com.moriku.healthcare_file_backend.dto.EmployeeProfileUpdateRequestDto;
import com.moriku.healthcare_file_backend.mapper.EmployeeProfileMapper;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmployeeProfileService {

    private final EmployeeProfileRepository employeeProfileRepository;

    public EmployeeProfileService(EmployeeProfileRepository employeeProfileRepository) {
        this.employeeProfileRepository = employeeProfileRepository;
    }

    private EmployeeProfile getEmployeeProfileOrThrow(Long userId) {
        return employeeProfileRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Employee profile does not exist for this userId: " + userId
            ));
    }

    public EmployeeProfileResponseDto getEmployeeProfile(Long userId) {
        EmployeeProfile profile = getEmployeeProfileOrThrow(userId);
        return EmployeeProfileMapper.toResponse(profile);
    }

    @Transactional
    public EmployeeProfileResponseDto patchEmployeeProfile(Long userId, EmployeeProfileUpdateRequestDto req) {
        EmployeeProfile profile = getEmployeeProfileOrThrow(userId);

        if (req.getWorkPhoneNumber() != null) profile.setWorkPhoneNumber(req.getWorkPhoneNumber());
        if (req.getPersonalPhoneNumber() != null) profile.setPersonalPhoneNumber(req.getPersonalPhoneNumber());
        if (req.getPersonalEmail() != null) profile.setPersonalEmail(req.getPersonalEmail());

        return EmployeeProfileMapper.toResponse(profile);
    }

}
