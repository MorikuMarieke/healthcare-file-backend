package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.UserCreateRequestDto;
import com.moriku.healthcare_file_backend.dto.UserCreateResponseDto;
import com.moriku.healthcare_file_backend.dto.UserRegistrationRequestDto;
import com.moriku.healthcare_file_backend.dto.UserResponseDto;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.Role;
import com.moriku.healthcare_file_backend.model.User;

public final class UserMapper {

    private UserMapper() {}

    // Registration Request -> User entity (account-only)
    public static User toEntity(UserRegistrationRequestDto req, Role role, String encodedPassword) {
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setMustChangePassword(false); // registration sets a real password
        return user;
    }

    public static User toEntity(UserCreateRequestDto req, Role role, String encodedPassword) {
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setMustChangePassword(true); // staff created -> temp password
        return user;
    }


    // Entity -> Response DTO (account-only)
    public static UserResponseDto toResponse(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getEmail(),
            user.getRole().getName(),
            user.isMustChangePassword(),
            user.getCreatedAt()
        );
    }

    // Entity -> Create Response DTO (account-only + temp password)
    public static UserCreateResponseDto toCreateResponse(User user, String temporaryPassword) {
        return new UserCreateResponseDto(
            user.getId(),
            user.getEmail(),
            user.getRole().getName(),
            temporaryPassword,
            user.isMustChangePassword()
        );
    }

    public static EmployeeProfile toEmployeeProfile(UserCreateRequestDto req, User savedUser) {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setUser(savedUser);
        profile.setFirstName(req.getFirstName());
        profile.setLastName(req.getLastName());

        // only set if your EmployeeProfile columns are nullable
        if (req.getWorkPhoneNumber() != null) profile.setWorkPhoneNumber(req.getWorkPhoneNumber());
        if (req.getPersonalPhoneNumber() != null) profile.setPersonalPhoneNumber(req.getPersonalPhoneNumber());
        if (req.getPersonalEmail() != null) profile.setPersonalEmail(req.getPersonalEmail());

        return profile;
    }
}
