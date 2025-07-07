package com.lms.studentmanagement.repository;

import com.lms.studentmanagement.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}