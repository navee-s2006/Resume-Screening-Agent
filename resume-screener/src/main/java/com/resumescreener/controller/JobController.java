package com.resumescreener.controller;

import com.resumescreener.dto.JobRequestDTO;
import com.resumescreener.entity.JobDescription;
import com.resumescreener.repository.JobRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*") // frontend HTML/JS vera port la irundhalum call panna allow pandrom
public class JobController {

    private final JobRepository jobRepository;

    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // Puthu job description create panna
    @PostMapping
    public JobDescription createJob(@RequestBody JobRequestDTO dto) {
        JobDescription job = new JobDescription();
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setRequiredSkills(dto.getRequiredSkills());
        return jobRepository.save(job);
    }

    // Ella jobs um list pandrathu
    @GetMapping
    public List<JobDescription> getAllJobs() {
        return jobRepository.findAll();
    }

    // Oru specific job details eduka
    @GetMapping("/{id}")
    public JobDescription getJob(@PathVariable Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found: " + id));
    }
}
