package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.UserCreateRequestDto;
import com.moriku.healthcare_file_backend.dto.UserRegistrationResponseDto;
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

    public List<UserRegistrationResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(UserMapper::toResponse)
            .toList();
    }

    public UserRegistrationResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        return UserMapper.toResponse(user);
    }

    public UserRegistrationResponseDto createUser(UserCreateRequestDto req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByBsn(req.getBsn())) {
            throw new IllegalArgumentException("BSN already exists");
        }

        String roleName = req.getRole().trim().toUpperCase();

        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        String encodedPassword = passwordEncoder.encode(req.getPassword());

        // you can either:
        // 1) add an overload in UserMapper for UserCreateRequestDto
        // or 2) build User directly here

        User user = new User(
            req.getBsn(),
            req.getEmail(),
            encodedPassword,
            req.getFirstName(),
            req.getLastName(),
            role
        );

        User saved = userRepository.save(user);
        return UserMapper.toResponse(saved);
    }

}
