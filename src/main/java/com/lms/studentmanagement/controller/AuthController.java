package com.lms.studentmanagement.controller;


import com.lms.studentmanagement.dto.LoginRequest;
import com.lms.studentmanagement.dto.LoginResponse;
import com.lms.studentmanagement.model.User;
import com.lms.studentmanagement.repository.UserRepository;
import com.lms.studentmanagement.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        LoginResponse response = new LoginResponse();
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(request.getPassword())) {
                response.setSuccess(true);
                response.setMessage("Login successful");
                response.setUserId(user.getId());
                response.setName(user.getName());
                response.setEmail(user.getEmail());
                response.setToken(
                        jwtUtil.generateToken(
                                user.getId(),
                                user.getEmail(),
                                user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet())
                        )
                );
                return ResponseEntity.ok(response);
            }
        }
        response.setSuccess(false);
        response.setMessage("Invalid email or password");
        return ResponseEntity.status(401).body(response);
    }
}