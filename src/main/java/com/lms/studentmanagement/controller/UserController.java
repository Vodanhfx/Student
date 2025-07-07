package com.lms.studentmanagement.controller;

import com.lms.studentmanagement.dto.CourseDTO;
import com.lms.studentmanagement.dto.UserCreateDTO;
import com.lms.studentmanagement.dto.UserDTO;
import com.lms.studentmanagement.mapper.CourseMapper;
import com.lms.studentmanagement.model.Course;
import com.lms.studentmanagement.model.Role;
import com.lms.studentmanagement.model.User;
import com.lms.studentmanagement.repository.RoleRepository;
import com.lms.studentmanagement.service.UserService;
import com.lms.studentmanagement.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    // Create new user (admin only)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setCreatedAt(new java.util.Date());
        user.setActive(true);


        Set<Role> roles = userDto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        user.setRoles(roles);

        User saved = userService.createUser(user);
        return ResponseEntity.ok(UserServiceImpl.toDTO(saved));
    }

    // Get all users (admin only)
    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserServiceImpl::toDTO)
                .collect(Collectors.toList());
    }

    // Get user by id (admin only)
    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return UserServiceImpl.toDTO(userService.getUserById(id));
    }

    // Disable user (admin only)
    //@PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return ResponseEntity.ok().build();
    }

    // Activate user (admin only)
    //@PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    // Enroll in a course (student only)
    //@PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{userId}/enroll/{courseId}")
    public ResponseEntity<Void> enrollCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        userService.enrollCourse(userId, courseId);
        return ResponseEntity.ok().build();
    }

    // Unenroll from a course (student only)
    // @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/{userId}/enroll/{courseId}")
    public ResponseEntity<Void> unenrollCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        userService.unenrollCourse(userId, courseId);
        return ResponseEntity.ok().build();
    }

    // List enrolled courses (student only) - DO NOT expose admin info
    //@PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{userId}/courses")
    public ResponseEntity<List<CourseDTO>> getEnrolledCourses(@PathVariable Long userId) {
        Set<Course> enrolledCourses = userService.getEnrolledCourses(userId);
        List<CourseDTO> courseDTOS = enrolledCourses.stream()
                .map(CourseMapper::toCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courseDTOS);
    }
}