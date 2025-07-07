package com.lms.studentmanagement.dto.Exam;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamDTO {
    private Long id;
    private Long lessonId;
    private List<QuestionDTO> questions;
    private int durationMinutes;
    private String location;
    private String timeslot;
    public void setTimeslot(String timeslot) { this.timeslot = timeslot; }
}
