package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.UserLoginRequestDto;
import com.moriku.healthcare_file_backend.dto.UserLoginResponseDto;
import com.moriku.healthcare_file_backend.dto.UserRegistrationRequestDto;
import com.moriku.healthcare_file_backend.dto.UserResponseDto;
import com.moriku.healthcare_file_backend.mapper.UserMapper;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.Role;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import com.moriku.healthcare_file_backend.repository.RoleRepository;
import com.moriku.healthcare_file_backend.repository.UserRepository;
import com.moriku.healthcare_file_backend.security.JwtUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientProfileRepository clientProfileRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ClientProfileRepository clientProfileRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientProfileRepository = clientProfileRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public UserResponseDto registerClient(UserRegistrationRequestDto req) {

        String email = req.getEmail().trim().toLowerCase();
        String bsn = req.getBsn().trim();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        ClientProfile profile = clientProfileRepository.findByBsn(bsn)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "ClientProfile not found for BSN: " + bsn
            ));

        if (profile.getUser() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This client file already has an account");
        }

        if (profile.getContactDetails() == null
            || profile.getContactDetails().getEmail() == null
            || profile.getContactDetails().getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contact email is not set for this client file");
        }

        String expectedEmail = profile.getContactDetails().getEmail().trim().toLowerCase();

        if (!expectedEmail.equals(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email does not match our records");
        }

        Role clientRole = roleRepository.findByName("CLIENT")
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "CLIENT role not found. Check data.sql seeding."
            ));

        String encodedPassword = passwordEncoder.encode(req.getPassword());

        User user = UserMapper.toEntity(req, clientRole, encodedPassword);
        user.setPasswordChangedAt(Instant.now());

        User saved = userRepository.save(user);

        profile.setUser(saved);
        // inside @Transactional this is optional if profile is managed
        // clientProfileRepository.save(profile);

        return UserMapper.toResponse(saved);
    }


    public UserLoginResponseDto login(UserLoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        return new UserLoginResponseDto(token);
    }

}
