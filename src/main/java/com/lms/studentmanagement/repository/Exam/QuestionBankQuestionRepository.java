package com.lms.studentmanagement.repository.Exam;

import com.lms.studentmanagement.model.Exam.QuestionBankQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionBankQuestionRepository extends JpaRepository<QuestionBankQuestion, Long> {
}