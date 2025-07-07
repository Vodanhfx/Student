package com.lms.studentmanagement.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.studentmanagement.dto.Exam.QuestionBankImportDTO;
import com.lms.studentmanagement.model.Exam.QuestionBankAnswer;
import com.lms.studentmanagement.model.Exam.QuestionBankQuestion;
import com.lms.studentmanagement.repository.Exam.QuestionBankQuestionRepository;
import com.lms.studentmanagement.service.QuestionBankService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    private final QuestionBankQuestionRepository questionRepo;

    public QuestionBankServiceImpl(QuestionBankQuestionRepository questionRepo) {
        this.questionRepo = questionRepo;
    }

    @Override
    public List<QuestionBankQuestion> importQuestionsFromJson(String json, Long ownerId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<QuestionBankImportDTO> dtos = mapper.readValue(json, new TypeReference<List<QuestionBankImportDTO>>() {});
            List<QuestionBankQuestion> saved = new ArrayList<>();
            for (QuestionBankImportDTO dto : dtos) {
                QuestionBankQuestion q = new QuestionBankQuestion();
                q.setContent(dto.getContent());
                q.setOwnerId(ownerId);

                List<QuestionBankAnswer> answers = new ArrayList<>();
                if (dto.getAnswers() != null) {
                    for (QuestionBankImportDTO.AnswerImportDTO adto : dto.getAnswers()) {
                        QuestionBankAnswer a = new QuestionBankAnswer();
                        a.setContent(adto.getContent());
                        a.setCorrect(adto.isCorrect());
                        a.setQuestion(q);
                        answers.add(a);
                    }
                }
                q.setAnswers(answers);

                saved.add(questionRepo.save(q));
            }
            return saved;
        } catch (Exception e) {
            throw new RuntimeException("Import failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<QuestionBankQuestion> importQuestionsFromFile(MultipartFile file, Long ownerId) {
        try {
            String json = new String(file.getBytes(), StandardCharsets.UTF_8);
            return importQuestionsFromJson(json, ownerId);
        } catch (Exception e) {
            throw new RuntimeException("Import failed: " + e.getMessage(), e);
        }
    }
}