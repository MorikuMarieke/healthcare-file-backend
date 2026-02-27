package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {

    boolean existsByBsn(String bsn);

    Optional<ClientProfile> findByBsn(String bsn);

    Optional<ClientProfile> findByUserId(Long userId);

    Optional<ClientProfile> findByUserIdAndActiveTrue(Long userId);

    Optional<EmployeeProfile> findByUser_Id(Long userId);

}
