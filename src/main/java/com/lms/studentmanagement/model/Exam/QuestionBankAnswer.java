package com.lms.studentmanagement.model.Exam;

import jakarta.persistence.*;

@Entity
public class QuestionBankAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionBankQuestion question;

    public Long getId() { return id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }

    public QuestionBankQuestion getQuestion() { return question; }
    public void setQuestion(QuestionBankQuestion question) { this.question = question; }
}