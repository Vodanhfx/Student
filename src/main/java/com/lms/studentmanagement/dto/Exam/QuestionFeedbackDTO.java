package com.lms.studentmanagement.dto.Exam;


import lombok.Data;

@Data
public class QuestionFeedbackDTO {
    private Long questionId;
    private String questionContent;
    private Long chosenAnswerId;
    private String chosenAnswerContent;
    private boolean correct;
    private Long correctAnswerId;
    private String correctAnswerContent;
}
