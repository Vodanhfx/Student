package com.lms.studentmanagement.service;

import com.lms.studentmanagement.dto.Exam.*;
import java.util.List;

public interface ExamService {
    ExamDTO getExamForLesson(Long lessonId);
    ExamResultDTO submitExamAttempt(ExamAttemptDTO attemptDTO);
    int getTriesLeft(Long lessonId, Long userId);
    List<ExamResultDTO> getExamHistory(Long examId, Long userId);

    ExamDTO createExam(ExamCreateDTO examCreateDTO, Long teacherId);
    ExamDTO updateExam(Long examId, ExamCreateDTO examCreateDTO, Long teacherId);
    void deleteExam(Long examId, Long teacherId);
    List<ExamDTO> getAllExamsForTeacher(Long teacherId);
}
