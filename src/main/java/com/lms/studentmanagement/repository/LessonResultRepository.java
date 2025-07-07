package com.lms.studentmanagement.repository;

import com.lms.studentmanagement.model.LessonResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonResultRepository extends JpaRepository<LessonResult, Long> {
}