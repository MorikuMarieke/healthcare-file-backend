package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.ClientAssignment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientAssignmentRepository extends JpaRepository<ClientAssignment, Long> {

    boolean existsByEmployeeProfileIdAndClientProfileId(Long employeeProfileId, Long clientProfileId);

    Optional<ClientAssignment> findByEmployeeProfileIdAndClientProfileId(Long employeeProfileId, Long clientProfileId);

    List<ClientAssignment> findAllByEmployeeProfileId(Long employeeProfileId);

    List<ClientAssignment> findAllByClientProfileId(Long clientProfileId);

    void deleteAllByEmployeeProfileId(Long employeeProfileId);
}