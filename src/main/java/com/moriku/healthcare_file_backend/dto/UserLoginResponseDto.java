package com.moriku.healthcare_file_backend.dto;

public class UserLoginResponseDto {

    private String token;

    public UserLoginResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
