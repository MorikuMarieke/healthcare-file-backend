package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.ClientProfileResponseDto;
import com.moriku.healthcare_file_backend.dto.EmployeeProfileResponseDto;
import com.moriku.healthcare_file_backend.dto.UserResponseDto;
import com.moriku.healthcare_file_backend.mapper.ClientProfileMapper;
import com.moriku.healthcare_file_backend.mapper.EmployeeProfileMapper;
import com.moriku.healthcare_file_backend.mapper.UserMapper;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import com.moriku.healthcare_file_backend.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MeService {

    private final UserRepository userRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    public MeService(
        UserRepository userRepository,
        ClientProfileRepository clientProfileRepository,
        EmployeeProfileRepository employeeProfileRepository
    ) {
        this.userRepository = userRepository;
        this.clientProfileRepository = clientProfileRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    public UserResponseDto getMe() {
        User user = getCurrentUserOrThrow();
        return UserMapper.toResponse(user);
    }

    public ClientProfileResponseDto getMyClientProfile() {
        User user = getCurrentUserOrThrow();

        if (!user.isClient()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only CLIENT can access /me/client-profile");
        }

        ClientProfile profile = clientProfileRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client profile not found"));

        return ClientProfileMapper.toResponse(profile);
    }

    public EmployeeProfileResponseDto getMyEmployeeProfile() {
        User user = getCurrentUserOrThrow();

        if (!user.isEmployee() && !user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only EMPLOYEE/ADMIN can access /me/employee-profile");
        }

        // If you used @MapsId, employeeProfile id == user id:
        EmployeeProfile profile = employeeProfileRepository.findById(user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee profile not found"));

        return EmployeeProfileMapper.toResponse(profile);
    }

    private User getCurrentUserOrThrow() {
        String email = SecurityUtils.getCurrentEmail();

        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }
}
