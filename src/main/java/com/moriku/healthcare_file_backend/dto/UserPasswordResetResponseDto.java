package com.moriku.healthcare_file_backend.dto;

public class UserPasswordResetResponseDto {

    private Long userId;
    private String temporaryPassword;

    public UserPasswordResetResponseDto() {}

    public UserPasswordResetResponseDto(Long userId, String temporaryPassword) {
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
