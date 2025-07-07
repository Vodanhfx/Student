package com.lms.studentmanagement.service;

import com.lms.studentmanagement.model.Exam.QuestionBankQuestion;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionBankService {
    List<QuestionBankQuestion> importQuestionsFromJson(String json, Long ownerId);
    List<QuestionBankQuestion> importQuestionsFromFile(MultipartFile file, Long ownerId);
}