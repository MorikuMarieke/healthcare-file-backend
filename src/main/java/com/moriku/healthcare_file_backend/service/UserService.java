package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.*;
import com.moriku.healthcare_file_backend.mapper.UserMapper;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.Role;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import com.moriku.healthcare_file_backend.repository.RoleRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeProfileRepository employeeProfileRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       EmployeeProfileRepository employeeProfileRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(UserMapper::toResponse)
            .toList();
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        return UserMapper.toResponse(user);
    }

    @Transactional
    public UserCreateResponseDto createUser(UserCreateRequestDto req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already exists");//TODO custom 409 exception handling
        }

        String roleName = req.getRole().trim().toUpperCase(); // EMPLOYEE or ADMIN (DTO pattern already restricts)
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        String tempPassword = generateTempPassword();
        String encodedPassword = passwordEncoder.encode(tempPassword);

        User user = UserMapper.toEntity(req, role, encodedPassword); // overload we add below
        User saved = userRepository.save(user);

        EmployeeProfile profile = UserMapper.toEmployeeProfile(req, saved); // method we add below
        employeeProfileRepository.save(profile);

        return UserMapper.toCreateResponse(saved, tempPassword);
    }

    private String generateTempPassword() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    public void changePassword(Long id, UserPasswordChangeRequestDto req) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!req.getNewPassword().equals(req.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }

        if (passwordEncoder.matches(req.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        user.setMustChangePassword(false);

        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

}
