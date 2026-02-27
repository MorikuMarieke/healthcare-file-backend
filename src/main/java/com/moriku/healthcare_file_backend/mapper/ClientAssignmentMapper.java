package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.assignment.ClientAssignmentResponse;
import com.moriku.healthcare_file_backend.model.ClientAssignment;

public final class ClientAssignmentMapper {

    private ClientAssignmentMapper() {
    }

    public static ClientAssignmentResponse toResponse(ClientAssignment assignment) {
        return new ClientAssignmentResponse(
            assignment.getId(),
            assignment.getEmployeeProfile().getId(),
            assignment.getClientProfile().getId(),
            assignment.getAssignedAt()
        );
    }
}
