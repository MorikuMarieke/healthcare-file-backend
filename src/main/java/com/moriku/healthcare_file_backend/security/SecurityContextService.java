package com.moriku.healthcare_file_backend.security;

import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SecurityContextService {

    private final UserRepository userRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final ClientProfileRepository clientProfileRepository;

    public SecurityContextService(
        UserRepository userRepository,
        EmployeeProfileRepository employeeProfileRepository,
        ClientProfileRepository clientProfileRepository
    ) {
        this.userRepository = userRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.clientProfileRepository = clientProfileRepository;
    }

    public User getCurrentUserOrThrow() {
        String email = SecurityUtils.getCurrentEmail();

        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    public EmployeeProfile getCurrentEmployeeProfileOrThrow() {
        User user = getCurrentUserOrThrow();

        if (!user.isEmployee() && !user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only EMPLOYEE/ADMIN can perform this action");
        }

        return employeeProfileRepository.findById(user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee profile not found"));
    }

    public void assertCurrentEmployeeHasAccessToClientForWriteOrThrow(ClientProfile clientProfile) {
        User currentUser = getCurrentUserOrThrow();

        if (currentUser.isAdmin()) {
            return;
        }

        EmployeeProfile employeeProfile = getCurrentEmployeeProfileOrThrow();

        if (employeeProfile.getCareTeam() == null || clientProfile.getCareTeam() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this client");
        }

        if (!employeeProfile.getCareTeam().getId().equals(clientProfile.getCareTeam().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No write access to this client");
        }
    }
}