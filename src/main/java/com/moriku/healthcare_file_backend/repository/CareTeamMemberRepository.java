package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.CareTeamMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CareTeamMemberRepository extends JpaRepository<CareTeamMember, Long> {

    List<CareTeamMember> findAllByCareTeamId(Long careTeamId);

    boolean existsByCareTeamIdAndEmployeeProfileId(Long careTeamId, Long employeeProfileId);

    Optional<CareTeamMember> findByCareTeamIdAndEmployeeProfileId(Long careTeamId, Long employeeProfileId);

    void deleteByCareTeamIdAndEmployeeProfileId(Long careTeamId, Long employeeProfileId);

    @Query("""
        select (count(m) > 0)
        from CareTeamMember m
        join CareTeamClient c on c.careTeam.id = m.careTeam.id
        where m.employeeProfile.id = :employeeId
          and c.clientProfile.id = :clientId
    """)
    boolean existsSharedTeamAccess(
        @Param("employeeId") Long employeeId,
        @Param("clientId") Long clientId
    );
}
