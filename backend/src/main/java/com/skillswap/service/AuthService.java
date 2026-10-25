package com.skillswap.service;

import com.skillswap.dto.*;
import com.skillswap.entity.User;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.UserRepository;
import com.skillswap.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CreditService creditService;
    private final EmailService emailService;

    @Value("${app.credits.signup-bonus}")
    private int signupBonus;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil, CreditService creditService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.creditService = creditService;
        this.emailService = emailService;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ApiException("Email already registered", HttpStatus.CONFLICT);
        }
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setCreditsBalance(signupBonus);
        user = userRepository.save(user);

        creditService.recordBonus(user, signupBonus, "Signup bonus");
        emailService.sendWelcomeEmail(user.getEmail(), user.getName());

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, UserMapper.toDto(user, null, null));
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ApiException("Invalid email or password", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new ApiException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, UserMapper.toDto(user, null, null));
    }
}
