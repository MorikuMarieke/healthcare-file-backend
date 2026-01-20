

package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.UserCreateResponseDto;
import com.moriku.healthcare_file_backend.dto.UserRegistrationRequestDto;
import com.moriku.healthcare_file_backend.dto.UserResponseDto;
import com.moriku.healthcare_file_backend.model.Role;
import com.moriku.healthcare_file_backend.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    // Request DTO -> Entity
    public static User toEntity(UserRegistrationRequestDto req, Role role, String encodedPassword) {
        User user = new User();
        user.setBsn(req.getBsn());
        user.setEmail(req.getEmail());
        user.setPassword(encodedPassword);
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setRole(role);
        return user;
    }

    // Entity -> Response DTO
    public static UserResponseDto toResponse(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRole().getName()
        );
    }

    public static UserCreateResponseDto toCreateResponse(User user, String temporaryPassword) {
        return new UserCreateResponseDto(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRole().getName(),
            temporaryPassword,
            user.isMustChangePassword()
        );
    }

}
