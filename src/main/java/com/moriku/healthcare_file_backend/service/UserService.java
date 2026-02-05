package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.*;
import com.moriku.healthcare_file_backend.mapper.UserMapper;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.Role;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import com.moriku.healthcare_file_backend.repository.RoleRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
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
    public UserCreateResponseDto createUser(UserCreateRequestDto dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        String roleName = dto.getRole();

        if (!"ADMIN".equals(roleName) && !"EMPLOYEE".equals(roleName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role must be ADMIN or EMPLOYEE");
        }

        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found: " + roleName));

        String tempPassword = generateTempPassword();
        String encodedPassword = passwordEncoder.encode(tempPassword);

        User user = UserMapper.toEntity(dto, role, encodedPassword);
        user.setPasswordChangedAt(Instant.now());

        User saved = userRepository.save(user);

        EmployeeProfile profile = UserMapper.toEmployeeProfile(dto, saved);
        employeeProfileRepository.save(profile);

        return UserMapper.toCreateResponse(saved, profile, tempPassword);
    }



    private String generateTempPassword() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    public void changePassword(Long id, UserPasswordChangeRequestDto dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setPasswordChangedAt(Instant.now());

        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
    }
}
