package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.client_profile.ClientProfileResponse;
import com.moriku.healthcare_file_backend.dto.employee_profile.EmployeeProfileResponse;
import com.moriku.healthcare_file_backend.dto.user.UserPasswordChangeRequest;
import com.moriku.healthcare_file_backend.dto.user.UserResponse;
import com.moriku.healthcare_file_backend.mapper.ClientProfileMapper;
import com.moriku.healthcare_file_backend.mapper.EmployeeProfileMapper;
import com.moriku.healthcare_file_backend.mapper.UserMapper;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import com.moriku.healthcare_file_backend.security.SecurityContextService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class MeService {

    private final ClientProfileRepository clientProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextService securityContextService;


    public MeService(
        ClientProfileRepository clientProfileRepository,
        EmployeeProfileRepository employeeProfileRepository,
        PasswordEncoder passwordEncoder,
        SecurityContextService securityContextService
    ) {
        this.clientProfileRepository = clientProfileRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityContextService = securityContextService;
    }

    public UserResponse getMe() {
        User user = securityContextService.getCurrentUserOrThrow();
        return UserMapper.toResponse(user);
    }

    public ClientProfileResponse getMyClientProfile() {
        ClientProfile profile = getActiveClientProfileForCurrentClientOrThrow();
        return ClientProfileMapper.toResponse(profile);
    }

    public EmployeeProfileResponse getMyEmployeeProfile() {
        User user = securityContextService.getCurrentUserOrThrow();

        if (!user.isEmployee() && !user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only EMPLOYEE/ADMIN can access /me/employee-profile");
        }

        // If you used @MapsId, employeeProfile id == user id:
        EmployeeProfile profile = employeeProfileRepository.findById(user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee profile not found"));

        return EmployeeProfileMapper.toResponse(profile);
    }

    public Long getMyEmployeeId() {
        User user = securityContextService.getCurrentUserOrThrow();

        if (!user.isEmployee() && !user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only EMPLOYEE/ADMIN can perform this action");
        }

        boolean exists = employeeProfileRepository.existsById(user.getId());
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee profile not found");
        }

        return user.getId();
    }

    @Transactional
    public void changeMyPassword(UserPasswordChangeRequest dto) {
        User user = securityContextService.getCurrentUserOrThrow();

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is incorrect");
        }

        if (dto.getCurrentPassword().equals(dto.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be different from old password");
        }


        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setPasswordChangedAt(Instant.now());
    }

    public ClientProfile getActiveClientProfileForCurrentClientOrThrow() {
        User user = securityContextService.getCurrentUserOrThrow();

        if (!user.isClient()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only CLIENT can access this endpoint");
        }

        ClientProfile profile = clientProfileRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client profile not found"));

        if (!profile.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Client profile is inactive");
        }

        return profile;
    }

}
