package com.lms.studentmanagement.repository.Exam;

import com.lms.studentmanagement.model.Exam.QuestionBankAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionBankAnswerRepository extends JpaRepository<QuestionBankAnswer, Long> {
}