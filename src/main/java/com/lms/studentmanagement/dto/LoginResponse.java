package com.lms.studentmanagement.dto;


import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private String message;
    private Long userId;
    private String name;
    private String email;
    private String token; // JWT token
}
