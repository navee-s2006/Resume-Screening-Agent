package com.resumescreener.dto;

import java.util.List;

// Gemini oda JSON response idhu format ku thaan parse pandrom
public class ScreeningResponseDTO {
    private Integer matchScore;
    private List<String> strengths;
    private List<String> gaps;
    private String verdict;

    public Integer getMatchScore() { return matchScore; }
    public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }

    public List<String> getStrengths() { return strengths; }
    public void setStrengths(List<String> strengths) { this.strengths = strengths; }

    public List<String> getGaps() { return gaps; }
    public void setGaps(List<String> gaps) { this.gaps = gaps; }

    public String getVerdict() { return verdict; }
    public void setVerdict(String verdict) { this.verdict = verdict; }
}
