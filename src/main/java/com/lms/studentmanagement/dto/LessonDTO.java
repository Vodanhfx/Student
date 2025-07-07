package com.lms.studentmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LessonDTO {
    private Long id;
    private String title;
    private String content;
    private String videoLink;
    private Long courseId;
}