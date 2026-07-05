package com.resumescreener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    // Password ah hash panna, verify panna idha use pandrom.
    // Idhu standalone bean, full Spring Security filter chain illa,
    // adhunala existing REST endpoints ellam open ah than irukkum.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
