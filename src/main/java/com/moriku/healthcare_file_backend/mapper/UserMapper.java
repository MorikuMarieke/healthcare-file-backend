package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.UserCreateRequestDto;
import com.moriku.healthcare_file_backend.dto.UserCreateResponseDto;
import com.moriku.healthcare_file_backend.dto.UserRegistrationRequestDto;
import com.moriku.healthcare_file_backend.dto.UserResponseDto;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.Role;
import com.moriku.healthcare_file_backend.model.User;

import java.time.Instant;

public final class UserMapper {

    private UserMapper() {
    }

    // Registration Request -> User entity (account-only)
    public static User toEntity(UserRegistrationRequestDto dto, Role role, String encodedPassword) {
        User user = new User();
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setPasswordChangedAt(Instant.now());
        return user;
    }
    //Create Request -> For staff User entity
    public static User toStaffEntity(UserCreateRequestDto dto, Role role) {
        User user = new User();
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setRole(role);
        return user;
    }

    // Entity -> Response DTO (account-only)
    public static UserResponseDto toResponse(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getEmail(),
            user.getRole().getName(),
            user.getCreatedAt()
            );
    }

    // Entity -> Create Response DTO (account-only + temp password)
    public static UserCreateResponseDto toCreateResponse(User user, EmployeeProfile profile, String temporaryPassword) {
        return new UserCreateResponseDto(
            user.getId(),
            user.getEmail(),
            user.getRole().getName(),
            temporaryPassword,
            profile.getFirstName(),
            profile.getLastName(),
            user.getCreatedAt()
        );
    }

    public static EmployeeProfile toEmployeeProfile(UserCreateRequestDto dto) {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        if (dto.getWorkPhoneNumber() != null) profile.setWorkPhoneNumber(dto.getWorkPhoneNumber());
        if (dto.getPersonalPhoneNumber() != null) profile.setPersonalPhoneNumber(dto.getPersonalPhoneNumber());
        if (dto.getPersonalEmail() != null) profile.setPersonalEmail(dto.getPersonalEmail());

        return profile;
    }
}
