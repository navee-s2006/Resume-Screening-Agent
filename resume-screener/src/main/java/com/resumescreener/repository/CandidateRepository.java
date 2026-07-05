package com.resumescreener.repository;

import com.resumescreener.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    // User oda email vachi, adha most recent uploaded resume ah eduka (profile page ku)
    java.util.Optional<Candidate> findTopByEmailOrderByUploadedAtDesc(String email);
}
