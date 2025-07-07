package com.lms.studentmanagement.service.impl;

import com.lms.studentmanagement.dto.UserDTO;
import com.lms.studentmanagement.exception.ResourceNotFoundException;
import com.lms.studentmanagement.model.Course;
import com.lms.studentmanagement.model.Role;
import com.lms.studentmanagement.model.User;
import com.lms.studentmanagement.repository.CourseRepository;
import com.lms.studentmanagement.repository.RoleRepository;
import com.lms.studentmanagement.repository.UserRepository;
import com.lms.studentmanagement.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.courseRepository = courseRepository;
    }

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setActive(user.isActive());
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        }
        return dto;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        user.setActive(true);
        if (user.getCreatedAt() == null) user.setCreatedAt(new java.util.Date());
        User saved = userRepository.save(user);
        logger.info("Created user: {}", saved.getEmail());
        return saved;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void disableUser(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
        logger.info("Disabled user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void activateUser(Long id) {
        User user = getUserById(id);
        user.setActive(true);
        userRepository.save(user);
        logger.info("Activated user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void enrollCourse(Long userId, Long courseId) {
        User user = getUserById(userId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));
        user.getEnrolledCourses().add(course);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unenrollCourse(Long userId, Long courseId) {
        User user = getUserById(userId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));
        user.getEnrolledCourses().remove(course);
        userRepository.save(user);
    }

    @Override
    public Set<Course> getEnrolledCourses(Long userId) {
        User user = getUserById(userId);
        return user.getEnrolledCourses();
    }
}