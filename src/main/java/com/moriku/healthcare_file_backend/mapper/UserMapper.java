package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.user.UserCreateRequest;
import com.moriku.healthcare_file_backend.dto.user.UserInviteResponse;
import com.moriku.healthcare_file_backend.dto.user.UserRegistrationRequest;
import com.moriku.healthcare_file_backend.dto.user.UserResponse;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.Role;
import com.moriku.healthcare_file_backend.model.User;

import java.time.Instant;

public final class UserMapper {

    private UserMapper() {
    }

    // -------- Registration (CLIENT) --------
    public static User toEntity(UserRegistrationRequest dto, Role role, String encodedPassword) {
        User user = new User();
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setPasswordChangedAt(Instant.now());
        return user;
    }

    // -------- Staff create (ADMIN creates EMPLOYEE / ADMIN) --------
    public static User toStaffEntity(UserCreateRequest dto, Role role) {
        User user = new User();
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setRole(role);
        return user;
    }

    // -------- Employee profile --------
    public static EmployeeProfile toEmployeeProfile(UserCreateRequest dto) {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());

        if (dto.getWorkPhoneNumber() != null) {
            profile.setWorkPhoneNumber(dto.getWorkPhoneNumber());
        }
        if (dto.getPersonalPhoneNumber() != null) {
            profile.setPersonalPhoneNumber(dto.getPersonalPhoneNumber());
        }
        if (dto.getPersonalEmail() != null) {
            profile.setPersonalEmail(dto.getPersonalEmail());
        }

        return profile;
    }

    // -------- Generic user response --------
    public static UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getRole().getName(),
            user.getCreatedAt()
        );
    }

    // -------- Invite response (POST /users) --------
    public static UserInviteResponse toInviteResponse(
        User user,
        String inviteToken,
        String inviteUrl
    ) {
        return new UserInviteResponse(
            user.getId(),
            user.getEmail(),
            user.getRole().getName(),
            inviteUrl,
            inviteToken
        );
    }

}
