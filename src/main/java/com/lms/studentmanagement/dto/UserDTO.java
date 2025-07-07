package com.lms.studentmanagement.dto;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private Date createdAt;
    private boolean active;
    private Set<String> roles;
}
