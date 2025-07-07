package com.lms.studentmanagement.controller;

import com.lms.studentmanagement.model.Exam.QuestionBankQuestion;
import com.lms.studentmanagement.service.QuestionBankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/questionbank")
public class QuestionBankController {

    private final QuestionBankService service;

    public QuestionBankController(QuestionBankService service) {
        this.service = service;
    }

    @PostMapping("/import")
    public ResponseEntity<List<QuestionBankQuestion>> importQuestions(
            @RequestParam("file") MultipartFile file,
            @RequestParam("ownerId") Long ownerId
    ) {
        List<QuestionBankQuestion> imported = service.importQuestionsFromFile(file, ownerId);
        return ResponseEntity.ok(imported);
    }
}