package com.resumescreener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResumeScreenerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResumeScreenerApplication.class, args);
        System.out.println("=================================================");
        System.out.println("  Resume Screener Backend Running Successfully  ");
        System.out.println("  http://localhost:8080                        ");
        System.out.println("=================================================");
    }
}

