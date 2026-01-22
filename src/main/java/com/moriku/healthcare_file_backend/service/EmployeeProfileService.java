package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.EmployeeProfileResponseDto;
import com.moriku.healthcare_file_backend.dto.EmployeeProfileUpdateRequestDto;
import com.moriku.healthcare_file_backend.mapper.EmployeeProfileMapper;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeProfileService {

    private final EmployeeProfileRepository employeeProfileRepository;

    public EmployeeProfileService(EmployeeProfileRepository employeeProfileRepository, UserRepository userRepository) {
        this.employeeProfileRepository = employeeProfileRepository;
    }

    public EmployeeProfileResponseDto getEmployeeProfile(Long userId) {
        EmployeeProfile profile = employeeProfileRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("EmployeeProfile not found for user id: " + userId));
        return EmployeeProfileMapper.toResponse(profile);
    }

    public EmployeeProfileResponseDto patchEmployeeProfile(Long userId, EmployeeProfileUpdateRequestDto req) {
        EmployeeProfile profile = employeeProfileRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("EmployeeProfile not found for user id: " + userId));

        if (req.getWorkPhoneNumber() != null) {
            profile.setWorkPhoneNumber(req.getWorkPhoneNumber());
        }
        if (req.getPersonalPhoneNumber() != null) {
            profile.setPersonalPhoneNumber(req.getPersonalPhoneNumber());
        }
        if (req.getPersonalEmail() != null) {
            profile.setPersonalEmail(req.getPersonalEmail());
        }

        EmployeeProfile saved = employeeProfileRepository.save(profile);
        return EmployeeProfileMapper.toResponse(saved);
    }

    public void deleteEmployeeProfile(Long userId) {
        if (!employeeProfileRepository.existsById(userId)) {
            throw new IllegalArgumentException("EmployeeProfile not found for user id: " + userId);
        }
        employeeProfileRepository.deleteById(userId);
    }
}
