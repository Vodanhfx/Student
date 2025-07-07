package com.lms.studentmanagement.controller;


import com.lms.studentmanagement.dto.Exam.*;
import com.lms.studentmanagement.service.ExamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/lesson/{lessonId}")
    public ExamDTO getExamForLesson(@PathVariable Long lessonId) {
        return examService.getExamForLesson(lessonId);
    }

    @PostMapping("/submit")
    public ExamResultDTO submitExamAttempt(@RequestBody ExamAttemptDTO attemptDTO) {
        return examService.submitExamAttempt(attemptDTO);
    }

    @GetMapping("/lesson/{lessonId}/tries/{userId}")
    public int getTriesLeft(@PathVariable Long lessonId, @PathVariable Long userId) {
        return examService.getTriesLeft(lessonId, userId);
    }

    @GetMapping("/history/{examId}/{userId}")
    public List<ExamResultDTO> getExamHistory(@PathVariable Long examId, @PathVariable Long userId) {
        return examService.getExamHistory(examId, userId);
    }

    @PostMapping("/create")
    public ExamDTO createExam(@RequestBody ExamCreateDTO examCreateDTO, @RequestParam Long teacherId) {
        return examService.createExam(examCreateDTO, teacherId);
    }

    @PutMapping("/{examId}")
    public ExamDTO updateExam(@PathVariable Long examId, @RequestBody ExamCreateDTO examCreateDTO, @RequestParam Long teacherId) {
        return examService.updateExam(examId, examCreateDTO, teacherId);
    }

    @DeleteMapping("/{examId}")
    public void deleteExam(@PathVariable Long examId, @RequestParam Long teacherId) {
        examService.deleteExam(examId, teacherId);
    }

    @GetMapping("/teacher/{teacherId}")
    public List<ExamDTO> getAllExamsForTeacher(@PathVariable Long teacherId) {
        return examService.getAllExamsForTeacher(teacherId);
    }
}