package com.moriku.healthcare_file_backend.dto.user;

public class UserLoginResponse {

    private String token;

    public UserLoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
