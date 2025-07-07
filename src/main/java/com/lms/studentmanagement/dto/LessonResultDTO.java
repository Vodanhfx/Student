package com.lms.studentmanagement.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LessonResultDTO {
    private Long id;
    private Long lessonId;
    private Long userId;
    private String status;
    private String uploadedFile;
    private String comment;
    private Date updatedAt;
}