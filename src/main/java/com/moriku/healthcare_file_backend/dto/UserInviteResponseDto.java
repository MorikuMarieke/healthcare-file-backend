package com.moriku.healthcare_file_backend.dto;

public class UserInviteResponseDto {
    private Long userId;
    private String email;
    private String role;
    private String inviteUrl;

    public UserInviteResponseDto(Long userId, String email, String role, String inviteUrl) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.inviteUrl = inviteUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getInviteUrl() {
        return inviteUrl;
    }

    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }
}

