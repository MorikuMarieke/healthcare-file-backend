package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByCarePlanIdOrderByCreatedAtDesc(Long carePlanId);

    Optional<Report> findByIdAndCarePlanId(Long reportId, Long carePlanId);

}