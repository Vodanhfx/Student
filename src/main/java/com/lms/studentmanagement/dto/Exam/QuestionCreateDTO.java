package com.lms.studentmanagement.dto.Exam;

import java.util.List;

public class QuestionCreateDTO {
    private String content;
    private List<AnswerCreateDTO> answers;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AnswerCreateDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerCreateDTO> answers) {
        this.answers = answers;
    }
}
