package com.lms.studentmanagement.service.impl;

import com.lms.studentmanagement.exception.ResourceNotFoundException;
import com.lms.studentmanagement.model.Course;
import com.lms.studentmanagement.model.User;
import com.lms.studentmanagement.repository.CourseRepository;
import com.lms.studentmanagement.repository.UserRepository;
import com.lms.studentmanagement.service.CourseService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Course createCourse(Course course, Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found: " + adminId));
        course.setCreatedBy(admin);
        Course saved = courseRepository.save(course);
        logger.info("Created course: {}", saved.getTitle());
        return saved;
    }

    @Override
    @Transactional
    public Course updateCourse(Long courseId, Course course, Long adminId) {
        Course existing = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));
        existing.setTitle(course.getTitle());
        existing.setDescription(course.getDescription());
        existing.setPublishedAt(course.getPublishedAt());
        logger.info("Updated course: {}", existing.getTitle());
        return courseRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteCourse(Long courseId, Long adminId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found: " + courseId);
        }
        courseRepository.deleteById(courseId);
        logger.info("Deleted course: {}", courseId);
    }

    @Override
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}