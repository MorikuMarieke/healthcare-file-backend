

package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.UserRegistrationRequestDto;
import com.moriku.healthcare_file_backend.dto.UserRegistrationResponseDto;
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
    public static UserRegistrationResponseDto toResponse(User user) {
        return new UserRegistrationResponseDto(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRole().getName()
        );
    }
}
