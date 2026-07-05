package com.resumescreener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumescreener.dto.ScreeningResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-1.5-flash}")
    private String model;

    // Gemini quota/billing issue vandha, demo ku fallback mock response use pandrathu.
    @Value("${gemini.use.mock:false}")
    private boolean useMock;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * PROMPT ENGINEERING part:
     * - Role assignment (recruiter persona)
     * - Clear numbered instructions (task decomposition)
     * - Strict output schema (JSON only) -> easy, reliable parsing
     * - Constraints to reduce hallucination
     */
    public String buildPrompt(String jobDescription, String requiredSkills, String resumeText) {
        return """
                You are an expert technical recruiter with 10+ years of experience screening resumes.

                TASK:
                Compare the CANDIDATE RESUME against the JOB DESCRIPTION below and evaluate the fit.
                Be strict and evidence-based. Do not assume skills that are not mentioned in the resume.

                JOB DESCRIPTION:
                %s

                REQUIRED SKILLS:
                %s

                CANDIDATE RESUME:
                %s

                INSTRUCTIONS:
                1. Give a match score from 0 to 100 based on skills match, relevant experience, and education fit.
                2. List up to 5 key strengths of the candidate relevant to this job.
                3. List up to 5 key gaps or missing skills compared to the job requirements.
                4. Give a final verdict, exactly one of: "Strong Match", "Moderate Match", "Not Suitable".
                5. Respond ONLY with valid JSON. No markdown, no code fences, no extra commentary.
                   Use exactly this schema:

                {
                  "matchScore": <integer 0-100>,
                  "strengths": ["point1", "point2"],
                  "gaps": ["point1", "point2"],
                  "verdict": "Strong Match | Moderate Match | Not Suitable"
                }
                """.formatted(jobDescription, requiredSkills, resumeText);
    }

    /**
     * Gemini API ku call pandrom. Quota/billing error (429) vandha, illa
     * gemini.use.mock=true na, resume content ah actual ah vachi analyze pannura
     * content-aware mock response ku fallback pandrom (pure random illa).
     */
    public String callGemini(String prompt, String requiredSkills, String resumeText) {
        if (useMock) {
            return generateContentAwareMockResponse(requiredSkills, resumeText);
        }

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + model + ":generateContent?key=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", prompt)))
                    ),
                    "generationConfig", Map.of(
                            "temperature", 0.2,
                            "maxOutputTokens", 1024
                    )
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            Map<?, ?> response = restTemplate.postForObject(url, entity, Map.class);
            return extractTextFromResponse(response);

        } catch (HttpClientErrorException.TooManyRequests e) {
            System.out.println("WARNING: Gemini quota exceeded, falling back to content-aware MOCK response.");
            return generateContentAwareMockResponse(requiredSkills, resumeText);
        }
    }

    /**
     * Mock mode-la irunthalum, unga ACTUAL resume text-ah vachithan score/strengths/gaps
     * calculate pandrom - pure random illa. Required skills ovvondrayum resume-la
     * mention pannirukka nu check pandrom, adha vachi realistic ah result generate pandrom.
     */
    private String generateContentAwareMockResponse(String requiredSkills, String resumeText) {
        String lowerResume = resumeText == null ? "" : resumeText.toLowerCase();

        List<String> skills = new ArrayList<>();
        if (requiredSkills != null) {
            for (String s : requiredSkills.split(",")) {
                String trimmed = s.trim();
                if (!trimmed.isEmpty()) skills.add(trimmed);
            }
        }

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        for (String skill : skills) {
            if (lowerResume.contains(skill.toLowerCase())) {
                matched.add(skill);
            } else {
                missing.add(skill);
            }
        }

        double skillRatio = skills.isEmpty() ? 0.5 : (double) matched.size() / skills.size();

        // Extra signals that suggest a stronger candidate, beyond exact skill keyword hits
        int bonus = 0;
        if (lowerResume.contains("project")) bonus += 8;
        if (lowerResume.contains("certificat")) bonus += 6;
        if (lowerResume.contains("intern")) bonus += 8;
        if (lowerResume.contains("experience")) bonus += 5;

        int score = (int) Math.round(20 + skillRatio * 60 + bonus);
        score = Math.max(8, Math.min(96, score));

        String verdict = score >= 72 ? "Strong Match" : (score >= 45 ? "Moderate Match" : "Not Suitable");

        List<String> strengths = new ArrayList<>();
        for (String skill : matched) {
            if (strengths.size() >= 4) break;
            strengths.add("Hands-on familiarity with " + skill);
        }
        if (lowerResume.contains("project")) strengths.add("Demonstrated project experience");
        if (lowerResume.contains("certificat") && strengths.size() < 5) strengths.add("Holds relevant certifications");
        if (strengths.isEmpty()) strengths.add("Resume shows a foundational technical background");

        List<String> gaps = new ArrayList<>();
        for (String skill : missing) {
            if (gaps.size() >= 5) break;
            gaps.add("No clear mention of " + skill + " in the resume");
        }
        if (gaps.isEmpty()) gaps.add("No major gaps identified for the listed required skills");

        try {
            Map<String, Object> mockResult = Map.of(
                    "matchScore", score,
                    "strengths", strengths,
                    "gaps", gaps,
                    "verdict", verdict
            );
            return objectMapper.writeValueAsString(mockResult);
        } catch (Exception e) {
            // Fallback plain string, should not normally happen
            return "{\"matchScore\":50,\"strengths\":[],\"gaps\":[],\"verdict\":\"Moderate Match\"}";
        }
    }

    @SuppressWarnings("unchecked")
    private String extractTextFromResponse(Map<?, ?> response) {
        try {
            List<Map<?, ?>> candidates = (List<Map<?, ?>>) response.get("candidates");
            Map<?, ?> content = (Map<?, ?>) candidates.get(0).get("content");
            List<Map<?, ?>> parts = (List<Map<?, ?>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response: " + response, e);
        }
    }

    /**
     * Gemini text response (JSON string) -> ScreeningResponseDTO object ku convert pandrom.
     * Gemini sometimes wraps JSON in ```json fences, so we clean that first.
     */
    public ScreeningResponseDTO parseScreeningResponse(String rawText) {
        try {
            String cleaned = rawText.replaceAll("```json", "").replaceAll("```", "").trim();
            return objectMapper.readValue(cleaned, ScreeningResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse screening JSON: " + rawText, e);
        }
    }
}
