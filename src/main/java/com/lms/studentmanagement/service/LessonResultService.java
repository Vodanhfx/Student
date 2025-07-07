package com.lms.studentmanagement.service;

import com.lms.studentmanagement.model.LessonResult;

public interface LessonResultService {
    LessonResult submitLessonResult(Long lessonId, LessonResult result, Long studentId);

    LessonResult reviewLessonResult(Long resultId, LessonResult.Status status, String comment, Long teacherId);
}
