package com.lms.studentmanagement.repository.Exam;

import com.lms.studentmanagement.model.Exam.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
