package com.lms.studentmanagement.service;

import com.lms.studentmanagement.model.Lesson;

public interface LessonService {
    Lesson createLesson(Long courseId, Lesson lesson, Long teacherId);

    Lesson updateLesson(Long lessonId, Lesson lesson, Long teacherId);

    void deleteLesson(Long lessonId, Long teacherId);
}
