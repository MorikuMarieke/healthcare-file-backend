package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.user.UserCreateRequest;
import com.moriku.healthcare_file_backend.dto.user.UserInviteResponse;
import com.moriku.healthcare_file_backend.dto.user.UserPasswordResetResponse;
import com.moriku.healthcare_file_backend.dto.user.UserResponse;
import com.moriku.healthcare_file_backend.exception.BadRequestException;
import com.moriku.healthcare_file_backend.exception.ConflictException;
import com.moriku.healthcare_file_backend.mapper.UserMapper;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.InviteToken;
import com.moriku.healthcare_file_backend.model.Role;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.EmployeeProfileRepository;
import com.moriku.healthcare_file_backend.repository.InviteTokenRepository;
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
    private final InviteTokenRepository inviteTokenRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       EmployeeProfileRepository employeeProfileRepository, InviteTokenRepository inviteTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.inviteTokenRepository = inviteTokenRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
            .map(UserMapper::toResponse)
            .toList();
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
        return UserMapper.toResponse(user);
    }

    public User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
    }

    @Transactional
    public UserInviteResponse createUser(UserCreateRequest dto) {

        String email = dto.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already exists: " + email);
        }

        String roleName = dto.getRole();

        if (!"ADMIN".equals(roleName) && !"EMPLOYEE".equals(roleName)) {
            throw new BadRequestException("Role must be ADMIN or EMPLOYEE");
        }

        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new BadRequestException("Role not found: " + roleName));

        User user = UserMapper.toStaffEntity(dto, role);

        // placeholder password: user cannot login until invite accept sets real password
        user.setPassword(passwordEncoder.encode(java.util.UUID.randomUUID().toString()));
        user.setPasswordChangedAt(null);

        EmployeeProfile profile = UserMapper.toEmployeeProfile(dto);
        user.setEmployeeProfile(profile);
        profile.setUser(user);

        User saved = userRepository.save(user);

        InviteToken invite = new InviteToken();
        invite.setToken(java.util.UUID.randomUUID().toString());
        invite.setUser(saved);
        invite.setExpiresAt(Instant.now().plus(1, java.time.temporal.ChronoUnit.DAYS));
        inviteTokenRepository.save(invite);

        String inviteToken = invite.getToken();

        String inviteUrl = "http://localhost:8080/auth/invite/accept?token=" + inviteToken;

        return UserMapper.toInviteResponse(saved, inviteToken, inviteUrl);
    }

    @Transactional //TODO: Consider adding user.setPasswordChangedAt(null); with this method, but this also needs to be added to CustomUserDetailsService so null will be treated as expired. This could also be used in maybe creation of user, but I'll consider.
    public UserPasswordResetResponse resetPassword(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));

        if (user.isClient()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client password reset not supported");
        }

        String tempPassword = generateTempPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setPasswordChangedAt(Instant.now());

        return new UserPasswordResetResponse(user.getId(), tempPassword);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private String generateTempPassword() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
