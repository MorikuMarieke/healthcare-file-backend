package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.*;
import com.moriku.healthcare_file_backend.mapper.UserMapper;
import com.moriku.healthcare_file_backend.model.Role;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.RoleRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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

    public UserCreateResponseDto createUser(UserCreateRequestDto req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByBsn(req.getBsn())) {
            throw new IllegalArgumentException("BSN already exists");
        }

        String roleName = req.getRole().trim().toUpperCase();

        // Staff provisioning endpoint: only EMPLOYEE/ADMIN
        if (!roleName.equals("EMPLOYEE") && !roleName.equals("ADMIN")) {
            throw new IllegalArgumentException("Role must be EMPLOYEE or ADMIN");
        }

        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        String tempPassword = generateTempPassword();
        String encodedPassword = passwordEncoder.encode(tempPassword);

        User user = new User(
            req.getBsn(),
            req.getEmail(),
            encodedPassword,
            req.getFirstName(),
            req.getLastName(),
            role
        );
        user.setMustChangePassword(true);

        User saved = userRepository.save(user);

        return UserMapper.toCreateResponse(saved, tempPassword);
    }

    private String generateTempPassword() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    public UserResponseDto patchUser(Long id, UserUpdateRequestDto req) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (req.getFirstName() != null) user.setFirstName(req.getFirstName());
        if (req.getLastName() != null) user.setLastName(req.getLastName());

        if (req.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setMustChangePassword(false); // optional, but makes sense
        }

        User saved = userRepository.save(user);
        return UserMapper.toResponse(saved);
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
