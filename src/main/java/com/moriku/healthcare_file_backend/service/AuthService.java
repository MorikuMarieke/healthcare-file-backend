package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.UserRegistrationRequestDto;
import com.moriku.healthcare_file_backend.dto.UserRegistrationResponseDto;
import com.moriku.healthcare_file_backend.mapper.UserMapper;
import com.moriku.healthcare_file_backend.model.Role;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.RoleRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserRegistrationResponseDto registerClient(UserRegistrationRequestDto req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByBsn(req.getBsn())) {
            throw new IllegalArgumentException("BSN already exists");
        }

        Role clientRole = roleRepository.findByName("CLIENT")
            .orElseThrow(() -> new IllegalStateException("CLIENT role not found. Check data.sql seeding."));

        String encodedPassword = passwordEncoder.encode(req.getPassword());

        User user = UserMapper.toEntity(req, clientRole, encodedPassword);

        User saved = userRepository.save(user);

        return UserMapper.toResponse(saved);
    }
}
