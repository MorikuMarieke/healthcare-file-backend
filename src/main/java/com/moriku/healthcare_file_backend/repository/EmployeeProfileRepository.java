package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

    Optional<EmployeeProfile> findByUser_Id(Long userId);
}
