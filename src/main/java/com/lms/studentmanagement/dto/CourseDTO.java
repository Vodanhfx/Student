package com.lms.studentmanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
@Data
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private Date publishedAt;
    private List<LessonDTO> lessons;
}
