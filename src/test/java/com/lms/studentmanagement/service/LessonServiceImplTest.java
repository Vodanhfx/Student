package com.lms.studentmanagement.service;

import com.lms.studentmanagement.model.Course;
import com.lms.studentmanagement.model.Lesson;
import com.lms.studentmanagement.repository.CourseRepository;
import com.lms.studentmanagement.repository.LessonRepository;
import com.lms.studentmanagement.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class LessonServiceImplTest {

    @Mock private LessonRepository lessonRepository;
    @Mock private CourseRepository courseRepository;

    @InjectMocks private LessonServiceImpl lessonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLesson_ShouldSaveLesson_WhenCourseExists() {
        Course course = new Course();
        course.setId(1L);
        Lesson lesson = new Lesson();
        lesson.setTitle("Lesson 1");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson saved = lessonService.createLesson(1L, lesson, 10L);

        assertThat(saved.getTitle()).isEqualTo("Lesson 1");
        verify(lessonRepository).save(lesson);
    }

    @Test
    void updateLesson_ShouldUpdateLesson_WhenExists() {
        Lesson existing = new Lesson();
        existing.setId(1L);
        existing.setTitle("Old");

        Lesson updated = new Lesson();
        updated.setTitle("New Title");
        updated.setContent("Updated Content");
        updated.setVideoLink("link");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(existing);

        Lesson result = lessonService.updateLesson(1L, updated, 10L);

        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getContent()).isEqualTo("Updated Content");
        assertThat(result.getVideoLink()).isEqualTo("link");
    }

    @Test
    void deleteLesson_ShouldCallDelete_WhenExists() {
        when(lessonRepository.existsById(1L)).thenReturn(true);
        doNothing().when(lessonRepository).deleteById(1L);

        lessonService.deleteLesson(1L, 10L);

        verify(lessonRepository).deleteById(1L);
    }
}