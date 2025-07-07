package com.lms.studentmanagement.service;

import com.lms.studentmanagement.exception.ResourceNotFoundException;
import com.lms.studentmanagement.model.Course;
import com.lms.studentmanagement.model.User;
import com.lms.studentmanagement.repository.CourseRepository;
import com.lms.studentmanagement.repository.UserRepository;
import com.lms.studentmanagement.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {
    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        courseRepository = mock(CourseRepository.class);
        userRepository = mock(UserRepository.class);
        courseService = new CourseServiceImpl(courseRepository, userRepository);
    }

    @Test
    void createCourse_shouldSaveWithAdmin() {
        User admin = new User();
        admin.setId(100L);
        Course course = new Course();
        course.setTitle("Test");
        when(userRepository.findById(100L)).thenReturn(Optional.of(admin));
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArgument(0));

        Course created = courseService.createCourse(course, 100L);

        assertThat(created.getCreatedBy()).isEqualTo(admin);
        verify(courseRepository).save(course);
    }

    @Test
    void createCourse_shouldThrowIfAdminNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        Course course = new Course();
        assertThatThrownBy(() -> courseService.createCourse(course, 999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateCourse_shouldUpdateFields() {
        Course old = new Course();
        old.setId(1L);
        old.setTitle("Old");
        old.setDescription("OldDesc");
        old.setPublishedAt(new Date());
        Course update = new Course();
        update.setTitle("New");
        update.setDescription("NewDesc");
        update.setPublishedAt(new Date());
        when(courseRepository.findById(1L)).thenReturn(Optional.of(old));
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArgument(0));

        Course updated = courseService.updateCourse(1L, update, 100L);

        assertThat(updated.getTitle()).isEqualTo("New");
        assertThat(updated.getDescription()).isEqualTo("NewDesc");
        verify(courseRepository).save(old);
    }

    @Test
    void updateCourse_shouldThrowIfNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> courseService.updateCourse(1L, new Course(), 100L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteCourse_shouldDeleteIfExists() {
        when(courseRepository.existsById(5L)).thenReturn(true);

        courseService.deleteCourse(5L, 100L);

        verify(courseRepository).deleteById(5L);
    }

    @Test
    void deleteCourse_shouldThrowIfNotExists() {
        when(courseRepository.existsById(5L)).thenReturn(false);

        assertThatThrownBy(() -> courseService.deleteCourse(5L, 100L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getCourseById_shouldReturnCourse() {
        Course c = new Course();
        c.setId(2L);
        when(courseRepository.findById(2L)).thenReturn(Optional.of(c));

        Course found = courseService.getCourseById(2L);

        assertThat(found.getId()).isEqualTo(2L);
    }

    @Test
    void getCourseById_shouldThrowIfNotFound() {
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> courseService.getCourseById(2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAllCourses_shouldReturnList() {
        List<Course> list = Arrays.asList(new Course(), new Course());
        when(courseRepository.findAll()).thenReturn(list);

        List<Course> result = courseService.getAllCourses();

        assertThat(result).hasSize(2);
    }
}
