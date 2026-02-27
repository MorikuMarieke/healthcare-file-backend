package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.assignment.ClientAssignmentResponse;
import com.moriku.healthcare_file_backend.service.ClientAssignmentService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assignments")
public class ClientAssignmentController {

    private final ClientAssignmentService clientAssignmentService;

    public ClientAssignmentController(ClientAssignmentService clientAssignmentService) {
        this.clientAssignmentService = clientAssignmentService;
    }

    @PostMapping("/employees/{employeeProfileId}/clients/{clientProfileId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientAssignmentResponse assign(
        @PathVariable Long employeeProfileId,
        @PathVariable Long clientProfileId
    ) {
        return clientAssignmentService.assignClientToEmployee(employeeProfileId, clientProfileId);
    }

    @DeleteMapping("/employees/{employeeProfileId}/clients/{clientProfileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unassign(
        @PathVariable Long employeeProfileId,
        @PathVariable Long clientProfileId
    ) {
        clientAssignmentService.unassignClientFromEmployee(employeeProfileId, clientProfileId);
    }

    @GetMapping("/employees/{employeeProfileId}/clients")
    public List<ClientAssignmentResponse> getForEmployee(@PathVariable Long employeeProfileId) {
        return clientAssignmentService.getAssignmentsForEmployee(employeeProfileId);
    }

    @GetMapping("/clients/{clientProfileId}/employees")
    public List<ClientAssignmentResponse> getForClient(@PathVariable Long clientProfileId) {
        return clientAssignmentService.getAssignmentsForClient(clientProfileId);
    }
}
