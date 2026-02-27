package com.moriku.healthcare_file_backend.dto.user;

public class UserInviteResponse {
    private Long userId;
    private String email;
    private String role;
    private String inviteUrl;
    private String inviteToken;

    public UserInviteResponse(Long userId, String email, String role, String inviteUrl, String inviteToken) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.inviteUrl = inviteUrl;
        this.inviteToken = inviteToken;
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

    public String getInviteToken() {
        return inviteToken;
    }

    public void setInviteToken(String inviteToken) {
        this.inviteToken = inviteToken;
    }
}

