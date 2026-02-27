package com.moriku.healthcare_file_backend.dto.client_profile;

import jakarta.validation.constraints.NotNull;

public class ClientProfileStatusRequest {

    @NotNull
    private Boolean active;

    public ClientProfileStatusRequest() {
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
