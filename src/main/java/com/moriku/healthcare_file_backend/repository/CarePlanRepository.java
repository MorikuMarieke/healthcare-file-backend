package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.CarePlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarePlanRepository extends JpaRepository<CarePlan, Long> {
    Optional<CarePlan> findByClientProfileId(Long clientProfileId);
    boolean existsByClientProfileId(Long clientProfileId);
}