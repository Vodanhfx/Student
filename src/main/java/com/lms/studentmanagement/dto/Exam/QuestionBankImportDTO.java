package com.lms.studentmanagement.dto.Exam;

import java.util.List;

public class QuestionBankImportDTO {
    private String content;
    private List<AnswerImportDTO> answers;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<AnswerImportDTO> getAnswers() { return answers; }
    public void setAnswers(List<AnswerImportDTO> answers) { this.answers = answers; }

    public static class AnswerImportDTO {
        private String content;
        private boolean correct;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public boolean isCorrect() { return correct; }
        public void setCorrect(boolean correct) { this.correct = correct; }
    }
}