package com.resumescreener.repository;

import com.resumescreener.entity.ScreeningResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScreeningResultRepository extends JpaRepository<ScreeningResult, Long> {

    // Job ID vachi ella results um match score descending order la eduka
    List<ScreeningResult> findByJobIdOrderByMatchScoreDesc(Long jobId);

    // Candidate ID vachi, adha ella screening history um recent first ah eduka (profile page ku)
    List<ScreeningResult> findByCandidateIdOrderByScreenedAtDesc(Long candidateId);

    // Oru email-ku (user account-ku) - avanga eppo evlo thadava resume upload pannalum,
    // ovvondrum thani thani candidate row-ah irundhalum, ella screening results-um
    // ontraaga, recent first-ah eduthukka idha use pandrom (profile history full-ah kaamikka).
    @Query("SELECT s FROM ScreeningResult s WHERE s.candidate.email = :email ORDER BY s.screenedAt DESC")
    List<ScreeningResult> findAllByCandidateEmailOrderByScreenedAtDesc(@Param("email") String email);
}
