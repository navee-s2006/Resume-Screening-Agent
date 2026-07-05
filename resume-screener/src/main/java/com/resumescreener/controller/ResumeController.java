package com.resumescreener.controller;

import com.resumescreener.entity.Candidate;
import com.resumescreener.repository.CandidateRepository;
import com.resumescreener.service.ResumeParserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "*")
public class ResumeController {

    private final CandidateRepository candidateRepository;
    private final ResumeParserService resumeParserService;

    public ResumeController(CandidateRepository candidateRepository,
                             ResumeParserService resumeParserService) {
        this.candidateRepository = candidateRepository;
        this.resumeParserService = resumeParserService;
    }

    // Resume file (PDF/DOCX) upload panna, text extract panni DB la save pandrom
    @PostMapping("/upload")
    public Candidate uploadResume(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "email", required = false) String email) throws IOException {

        String extractedText = resumeParserService.extractText(file);

        // Name explicit ah kudukala na (illa blank ah irundha), resume text-la irundhu
        // first line ah eduthu, adha candidate name ah use pandrom (mostly resume top-la
        // than candidate oda actual name irukkum, login user oda name illama).
        String candidateName = (name != null && !name.isBlank())
                ? name
                : extractNameFromResumeText(extractedText, file.getOriginalFilename());

        Candidate candidate = new Candidate();
        candidate.setName(candidateName);
        candidate.setEmail(email);
        candidate.setResumeFilePath(file.getOriginalFilename());
        candidate.setResumeText(extractedText);

        return candidateRepository.save(candidate);
    }

    // Resume-oda first meaningful line ah name ah eduthukurom (mostly resume top-la
    // "NAVEENA G" mathiri than irukum). Onnume kedaikala na, file name ah fallback ah use pandrom.
    private String extractNameFromResumeText(String text, String fallbackFileName) {
        if (text != null) {
            for (String line : text.split("\\r?\\n")) {
                String trimmed = line.trim();
                // Valid name line: not empty, not too long, no email/phone-looking characters
                if (!trimmed.isEmpty()
                        && trimmed.length() <= 60
                        && !trimmed.contains("@")
                        && !trimmed.matches(".*\\d{3,}.*")) {
                    return trimmed;
                }
            }
        }
        return fallbackFileName != null ? fallbackFileName.replaceAll("\\.(pdf|docx)$", "") : "Unknown Candidate";
    }

    // Ella candidates um list pandrathu
    @GetMapping
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @GetMapping("/{id}")
    public Candidate getCandidate(@PathVariable Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found: " + id));
    }

    // Logged-in user oda email vachi, adha latest uploaded resume ah eduka (profile page ku)
    @GetMapping("/by-email/{email}")
    public java.util.Optional<Candidate> getCandidateByEmail(@PathVariable String email) {
        return candidateRepository.findTopByEmailOrderByUploadedAtDesc(email);
    }
}
