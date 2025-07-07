package com.lms.studentmanagement.service;


import com.lms.studentmanagement.model.Lesson;
import com.lms.studentmanagement.model.LessonResult;
import com.lms.studentmanagement.model.User;
import com.lms.studentmanagement.repository.LessonRepository;
import com.lms.studentmanagement.repository.LessonResultRepository;
import com.lms.studentmanagement.repository.UserRepository;
import com.lms.studentmanagement.exception.ResourceNotFoundException;
import com.lms.studentmanagement.service.impl.LessonResultServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class LessonResultServiceImplTest {

    @Mock private LessonResultRepository lessonResultRepository;
    @Mock private LessonRepository lessonRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private LessonResultServiceImpl lessonResultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitLessonResult_ShouldSaveResult_WhenValid() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        User user = new User();
        user.setId(2L);
        LessonResult result = new LessonResult();
        result.setUploadedFile("file.txt");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(lessonResultRepository.save(any(LessonResult.class))).thenReturn(result);

        LessonResult saved = lessonResultService.submitLessonResult(1L, result, 2L);

        assertThat(saved.getUploadedFile()).isEqualTo("file.txt");
        verify(lessonResultRepository).save(result);
    }

    @Test
    void submitLessonResult_ShouldThrow_WhenLessonNotFound() {
        LessonResult result = new LessonResult();
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonResultService.submitLessonResult(1L, result, 2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Lesson not found");
    }

    @Test
    void reviewLessonResult_ShouldUpdateStatusAndComment() {
        LessonResult result = new LessonResult();
        result.setId(1L);

        when(lessonResultRepository.findById(1L)).thenReturn(Optional.of(result));
        when(lessonResultRepository.save(any(LessonResult.class))).thenReturn(result);

        LessonResult reviewed = lessonResultService.reviewLessonResult(1L, LessonResult.Status.DONE, "Good", 100L);

        assertThat(reviewed.getStatus()).isEqualTo(LessonResult.Status.DONE);
        assertThat(reviewed.getComment()).isEqualTo("Good");
    }
}
