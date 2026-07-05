package com.resumescreener.controller;

import com.resumescreener.dto.AuthDTOs;
import com.resumescreener.dto.UserResponseDTO;
import com.resumescreener.entity.User;
import com.resumescreener.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AuthDTOs.RegisterRequest req) {
        if (req.getUsername() == null || req.getEmail() == null || req.getPassword() == null
                || req.getUsername().isBlank() || req.getEmail().isBlank() || req.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(errorBody("Username, email and password are required"));
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody("Email already registered"));
        }
        if (userRepository.existsByUsername(req.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody("Username already taken"));
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setFullName(req.getFullName());

        User saved = userRepository.save(user);
        return ResponseEntity.ok(toDto(saved));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthDTOs.LoginRequest req) {
        return userRepository.findByEmail(req.getEmail())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPassword()))
                .<ResponseEntity<Object>>map(u -> ResponseEntity.ok(toDto(u)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorBody("Invalid email or password")));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .<ResponseEntity<Object>>map(u -> ResponseEntity.ok(toDto(u)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody("User not found")));
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<Object> updateProfile(@PathVariable Long id, @RequestBody AuthDTOs.ProfileUpdateRequest req) {
        return userRepository.findById(id).<ResponseEntity<Object>>map(u -> {
            u.setFullName(req.getFullName());
            u.setPhone(req.getPhone());
            u.setCollege(req.getCollege());
            u.setDegree(req.getDegree());
            u.setGraduationYear(req.getGraduationYear());
            u.setLocation(req.getLocation());
            u.setBio(req.getBio());
            return ResponseEntity.ok(toDto(userRepository.save(u)));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody("User not found")));
    }

    private UserResponseDTO toDto(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    private Object errorBody(String message) {
        return new Object() {
            public final String error = message;
        };
    }
}
