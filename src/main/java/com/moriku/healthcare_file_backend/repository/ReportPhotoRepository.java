package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.ReportPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportPhotoRepository extends JpaRepository<ReportPhoto, Long> {

    List<ReportPhoto> findAllByReportIdOrderByUploadedAtAsc(Long reportId);

    long countByReportId(Long reportId);

    Optional<ReportPhoto> findByIdAndReportId(Long photoId, Long reportId);
}