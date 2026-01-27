package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {
    Optional<ClientProfile> findByBsn(String bsn);
    boolean existsByBsn(String bsn);
}
