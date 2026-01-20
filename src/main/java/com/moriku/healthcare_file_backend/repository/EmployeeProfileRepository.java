package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    boolean existsByUser_Id(Long userId);
}
