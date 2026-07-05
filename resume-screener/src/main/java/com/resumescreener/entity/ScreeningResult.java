package com.resumescreener.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "screening_result")
public class ScreeningResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private JobDescription job;

    @Column(name = "match_score")
    private Integer matchScore;

    @Column(columnDefinition = "TEXT")
    private String strengths;

    @Column(columnDefinition = "TEXT")
    private String gaps;

    private String verdict;

    @Column(name = "llm_raw_response", columnDefinition = "LONGTEXT")
    private String llmRawResponse;

    @Column(name = "screened_at", updatable = false)
    private LocalDateTime screenedAt;

    @PrePersist
    public void prePersist() {
        this.screenedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }

    public JobDescription getJob() { return job; }
    public void setJob(JobDescription job) { this.job = job; }

    public Integer getMatchScore() { return matchScore; }
    public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }

    public String getStrengths() { return strengths; }
    public void setStrengths(String strengths) { this.strengths = strengths; }

    public String getGaps() { return gaps; }
    public void setGaps(String gaps) { this.gaps = gaps; }

    public String getVerdict() { return verdict; }
    public void setVerdict(String verdict) { this.verdict = verdict; }

    public String getLlmRawResponse() { return llmRawResponse; }
    public void setLlmRawResponse(String llmRawResponse) { this.llmRawResponse = llmRawResponse; }

    public LocalDateTime getScreenedAt() { return screenedAt; }
    public void setScreenedAt(LocalDateTime screenedAt) { this.screenedAt = screenedAt; }
}
