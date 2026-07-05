package com.resumescreener.controller;

import com.resumescreener.entity.ScreeningResult;
import com.resumescreener.service.ScreeningService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    // Oru candidate ah oru job ku etherku screen pandrathu -> Gemini call aagum
    @PostMapping("/screen/{candidateId}/{jobId}")
    public ScreeningResult screenCandidate(@PathVariable Long candidateId, @PathVariable Long jobId) {
        return screeningService.screenCandidate(candidateId, jobId);
    }

    // Oru job ku ella candidates oda ranked results (score descending)
    @GetMapping("/results/{jobId}")
    public List<ScreeningResult> getResults(@PathVariable Long jobId) {
        return screeningService.getResultsForJob(jobId);
    }

    // Oru candidate oda ella screening history um (profile page ku, latest score kaamikka)
    @GetMapping("/results/candidate/{candidateId}")
    public List<ScreeningResult> getResultsForCandidate(@PathVariable Long candidateId) {
        return screeningService.getResultsForCandidate(candidateId);
    }

    // User oda email vachi, avanga eppo evlo thadava scan pannirundhalum (daily edhavadhu
    // dhinam pannirundhalum) - ella screening results-um ontraaga history-la kaamikka.
    @GetMapping("/results/by-email/{email}")
    public List<ScreeningResult> getResultsForEmail(@PathVariable String email) {
        return screeningService.getResultsForEmail(email);
    }
}
