package com.resumescreener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumescreener.dto.ScreeningResponseDTO;
import com.resumescreener.entity.Candidate;
import com.resumescreener.entity.JobDescription;
import com.resumescreener.entity.ScreeningResult;
import com.resumescreener.repository.CandidateRepository;
import com.resumescreener.repository.JobRepository;
import com.resumescreener.repository.ScreeningResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreeningService {

    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final ScreeningResultRepository screeningResultRepository;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ScreeningService(CandidateRepository candidateRepository,
                             JobRepository jobRepository,
                             ScreeningResultRepository screeningResultRepository,
                             GeminiService geminiService) {
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
        this.screeningResultRepository = screeningResultRepository;
        this.geminiService = geminiService;
    }

    /**
     * Main flow: candidate + job eduthu, prompt build panni, Gemini ku anupi,
     * response ah parse panni, DB la save pandrom.
     */
    public ScreeningResult screenCandidate(Long candidateId, Long jobId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found: " + candidateId));

        JobDescription job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found: " + jobId));

        String prompt = geminiService.buildPrompt(
                job.getDescription(),
                job.getRequiredSkills(),
                candidate.getResumeText()
        );

        String rawResponse = geminiService.callGemini(prompt, job.getRequiredSkills(), candidate.getResumeText());
        ScreeningResponseDTO parsed = geminiService.parseScreeningResponse(rawResponse);

        ScreeningResult result = new ScreeningResult();
        result.setCandidate(candidate);
        result.setJob(job);
        result.setMatchScore(parsed.getMatchScore());
        result.setVerdict(parsed.getVerdict());
        result.setLlmRawResponse(rawResponse);

        try {
            result.setStrengths(objectMapper.writeValueAsString(parsed.getStrengths()));
            result.setGaps(objectMapper.writeValueAsString(parsed.getGaps()));
        } catch (Exception e) {
            result.setStrengths(String.valueOf(parsed.getStrengths()));
            result.setGaps(String.valueOf(parsed.getGaps()));
        }

        return screeningResultRepository.save(result);
    }

    public List<ScreeningResult> getResultsForJob(Long jobId) {
        return screeningResultRepository.findByJobIdOrderByMatchScoreDesc(jobId);
    }

    public List<ScreeningResult> getResultsForCandidate(Long candidateId) {
        return screeningResultRepository.findByCandidateIdOrderByScreenedAtDesc(candidateId);
    }

    // User oda email vachi, avanga eppo evlo thadava scan pannirundhalum,
    // ella screening results-um recent first-ah return pandrom (profile history ku).
    public List<ScreeningResult> getResultsForEmail(String email) {
        return screeningResultRepository.findAllByCandidateEmailOrderByScreenedAtDesc(email);
    }
}
