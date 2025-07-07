package com.lms.studentmanagement.service;


import com.lms.studentmanagement.model.Course;

import java.util.List;

public interface CourseService {
    Course createCourse(Course course, Long adminId);

    Course updateCourse(Long courseId, Course course, Long adminId);

    void deleteCourse(Long courseId, Long adminId);

    Course getCourseById(Long courseId);

    List<Course> getAllCourses();
}