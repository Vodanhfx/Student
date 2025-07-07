package com.lms.studentmanagement.model.Exam;

import com.lms.studentmanagement.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ExamAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Exam exam;

    @ManyToOne
    private User user;

    private int attemptNumber; // 3

    private int score;

    private boolean completed;
}