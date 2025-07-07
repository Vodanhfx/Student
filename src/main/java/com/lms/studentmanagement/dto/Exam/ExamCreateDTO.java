package com.lms.studentmanagement.dto.Exam;

import java.util.List;

public class ExamCreateDTO {
    private Long lessonId;
    private String title;
    private Integer durationMinutes;
    private String location;
    private String timeslot;
    private List<QuestionCreateDTO> questions;

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public List<QuestionCreateDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionCreateDTO> questions) {
        this.questions = questions;
    }
}