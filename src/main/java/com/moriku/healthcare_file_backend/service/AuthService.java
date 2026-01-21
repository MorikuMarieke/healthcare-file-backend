package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.UserRegistrationRequestDto;
import com.moriku.healthcare_file_backend.dto.UserResponseDto;
import com.moriku.healthcare_file_backend.mapper.UserMapper;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.Role;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import com.moriku.healthcare_file_backend.repository.RoleRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientProfileRepository clientProfileRepository;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       ClientProfileRepository clientProfileRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientProfileRepository = clientProfileRepository;
    }

    public UserResponseDto registerClient(UserRegistrationRequestDto req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        ClientProfile profile = clientProfileRepository.findByBsn(req.getBsn().trim())
            .orElseThrow(() -> new IllegalArgumentException("ClientProfile not found for BSN: " + req.getBsn()));

        if (profile.getUser() != null) {
            throw new IllegalArgumentException("This client file already has an account");
        }

        // Require contact email to be set on the client file before allowing registration
        if (profile.getContactDetails() == null ||
            profile.getContactDetails().getEmail() == null ||
            profile.getContactDetails().getEmail().isBlank()) {
            throw new IllegalArgumentException("Contact email is not set for this client file");
        }

        String expectedEmail = profile.getContactDetails().getEmail().trim().toLowerCase();
        String providedEmail = req.getEmail().trim().toLowerCase();

        if (!expectedEmail.equals(providedEmail)) {
            throw new IllegalArgumentException("Email does not match our records");
        }

        Role clientRole = roleRepository.findByName("CLIENT")
            .orElseThrow(() -> new IllegalStateException("CLIENT role not found. Check data.sql seeding."));

        String encodedPassword = passwordEncoder.encode(req.getPassword());

        User user = UserMapper.toEntity(req, clientRole, encodedPassword);
        User saved = userRepository.save(user);

        profile.setUser(saved);
        clientProfileRepository.save(profile);

        return UserMapper.toResponse(saved);
    }
}
