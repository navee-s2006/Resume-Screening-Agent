package com.resumescreener.repository;

import com.resumescreener.entity.JobDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<JobDescription, Long> {
}
