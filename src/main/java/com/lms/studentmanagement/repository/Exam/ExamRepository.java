package com.lms.studentmanagement.repository.Exam;

import com.lms.studentmanagement.model.Exam.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Exam findByLesson_Id(Long lessonId);
    List<Exam> findByCreatedBy_Id(Long createdById);
}