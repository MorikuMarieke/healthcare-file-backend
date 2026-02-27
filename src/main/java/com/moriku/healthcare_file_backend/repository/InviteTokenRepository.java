package com.moriku.healthcare_file_backend.repository;

import com.moriku.healthcare_file_backend.model.InviteToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InviteTokenRepository extends JpaRepository<InviteToken, String> {
    Optional<InviteToken> findByToken(String token);

    void deleteAllByUser_Id(Long userId);

}

