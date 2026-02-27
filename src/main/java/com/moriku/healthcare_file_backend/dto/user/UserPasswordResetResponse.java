package com.moriku.healthcare_file_backend.dto.user;

public class UserPasswordResetResponse {

    private Long userId;
    private String temporaryPassword;

    public UserPasswordResetResponse() {}

    public UserPasswordResetResponse(Long userId, String temporaryPassword) {
        this.userId = userId;
        this.temporaryPassword = temporaryPassword;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTemporaryPassword() {
        return temporaryPassword;
    }

    public void setTemporaryPassword(String temporaryPassword) {
        this.temporaryPassword = temporaryPassword;
    }
}
