package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.EmployeeProfileCreateRequestDto;
import com.moriku.healthcare_file_backend.dto.EmployeeProfileResponseDto;
import com.moriku.healthcare_file_backend.dto.EmployeeProfileUpdateRequestDto;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeProfileService {

    private final EmployeeProfileRepository employeeProfileRepository;
    private final UserRepository userRepository;

    public EmployeeProfileService(EmployeeProfileRepository employeeProfileRepository, UserRepository userRepository) {
        this.employeeProfileRepository = employeeProfileRepository;
        this.userRepository = userRepository;
    }

    public EmployeeProfileResponseDto createEmployeeProfile(Long userId, EmployeeProfileCreateRequestDto req) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (employeeProfileRepository.existsById(userId)) {
            throw new IllegalArgumentException("EmployeeProfile already exists for user id: " + userId);
        }

//        // Optional: enforce role rule already (even before security) //TODO: Maybe apply this later
//        String roleName = user.getRole().getName();
//        if (!roleName.equals("EMPLOYEE") && !roleName.equals("ADMIN")) {
//            throw new IllegalArgumentException("EmployeeProfile can only be created for EMPLOYEE or ADMIN users");
//        }

        EmployeeProfile profile = new EmployeeProfile();
        profile.setUser(user);
        profile.setWorkPhoneNumber(req.getWorkPhoneNumber());
        profile.setPersonalPhoneNumber(req.getPersonalPhoneNumber());
        profile.setPersonalEmail(req.getPersonalEmail());

        // keep bidirectional link consistent (if you added it in User)
        user.setEmployeeProfile(profile);

        EmployeeProfile saved = employeeProfileRepository.save(profile);
        return toResponse(saved);
    }

    public EmployeeProfileResponseDto getEmployeeProfile(Long userId) {
        EmployeeProfile profile = employeeProfileRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("EmployeeProfile not found for user id: " + userId));
        return toResponse(profile);
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
        return toResponse(saved);
    }

    public void deleteEmployeeProfile(Long userId) {
        if (!employeeProfileRepository.existsById(userId)) {
            throw new IllegalArgumentException("EmployeeProfile not found for user id: " + userId);
        }
        employeeProfileRepository.deleteById(userId);
    }

    private EmployeeProfileResponseDto toResponse(EmployeeProfile profile) {
        return new EmployeeProfileResponseDto(
            profile.getUser().getId(),
            profile.getUser().getEmail(),
            profile.getWorkPhoneNumber(),
            profile.getPersonalPhoneNumber(),
            profile.getPersonalEmail()
        );
    }
}
