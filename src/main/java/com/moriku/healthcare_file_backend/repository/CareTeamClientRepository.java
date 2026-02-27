package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.CareTeamClient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareTeamClientRepository extends JpaRepository<CareTeamClient, Long> {

    List<CareTeamClient> findAllByCareTeamId(Long careTeamId);

    boolean existsByCareTeamIdAndClientProfileId(Long careTeamId, Long clientProfileId);

    Optional<CareTeamClient> findByCareTeamIdAndClientProfileId(Long careTeamId, Long clientProfileId);

    void deleteByCareTeamIdAndClientProfileId(Long careTeamId, Long clientProfileId);
}
