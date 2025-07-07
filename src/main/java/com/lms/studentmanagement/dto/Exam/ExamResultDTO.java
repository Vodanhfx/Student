package com.lms.studentmanagement.dto.Exam;

import lombok.Data;
import java.util.List;

@Data
public class ExamResultDTO {
    private int score;
    private int totalQuestions;
    private int attemptNumber;
    private boolean completed;
    private List<QuestionFeedbackDTO> questionFeedbacks;
}