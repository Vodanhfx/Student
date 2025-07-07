package com.lms.studentmanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class LessonResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Lesson lesson;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status; // IN_PROGRESS, DONE

    private String uploadedFile;
    private String comment;
    private Date updatedAt;

    public enum Status {
        IN_PROGRESS, DONE
    }
}