package com.moriku.healthcare_file_backend.dto.assignment;

import java.time.LocalDateTime;

public class ClientAssignmentResponse {

    private Long id;
    private Long employeeProfileId;
    private Long clientProfileId;
    private LocalDateTime assignedAt;

    public ClientAssignmentResponse(Long id, Long employeeProfileId, Long clientProfileId, LocalDateTime assignedAt) {
        this.id = id;
        this.employeeProfileId = employeeProfileId;
        this.clientProfileId = clientProfileId;
        this.assignedAt = assignedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getEmployeeProfileId() {
        return employeeProfileId;
    }

    public Long getClientProfileId() {
        return clientProfileId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
}
