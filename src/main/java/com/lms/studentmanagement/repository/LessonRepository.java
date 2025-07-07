package com.lms.studentmanagement.repository;

import com.lms.studentmanagement.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByTeacherId(Long teacherId);
}