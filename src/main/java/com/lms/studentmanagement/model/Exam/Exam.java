package com.lms.studentmanagement.model.Exam;

import com.lms.studentmanagement.model.Lesson;
import com.lms.studentmanagement.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Lesson lesson;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    private int durationMinutes;

    private String location;

    private LocalDateTime timeslot;
}