package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.Goal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findAllByCarePlanId(Long carePlanId);

    Optional<Goal> findByIdAndCarePlanId(Long id, Long carePlanId);
}