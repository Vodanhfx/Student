package com.lms.studentmanagement.repository.Exam;

import com.lms.studentmanagement.model.Exam.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
    int countByExamIdAndUserId(Long examId, Long userId);

    List<ExamAttempt> findByExamIdAndUserIdOrderByAttemptNumberAsc(Long examId, Long userId);
}
