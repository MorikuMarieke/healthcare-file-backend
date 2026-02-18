package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.CareTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareTeamRepository extends JpaRepository<CareTeam, Long> {
    boolean existsByTeamNameIgnoreCase(String teamName);
    boolean existsByTeamEmailIgnoreCase(String teamEmail);
    boolean existsByTeamPhoneNumber(String teamPhoneNumber);
}
