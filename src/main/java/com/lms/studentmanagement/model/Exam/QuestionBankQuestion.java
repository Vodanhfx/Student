package com.lms.studentmanagement.model.Exam;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class QuestionBankQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionBankAnswer> answers;

    private Long ownerId;

    public Long getId() { return id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<QuestionBankAnswer> getAnswers() { return answers; }
    public void setAnswers(List<QuestionBankAnswer> answers) { this.answers = answers; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}