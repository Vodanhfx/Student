package com.lms.studentmanagement.service;

import com.lms.studentmanagement.model.Course;
import com.lms.studentmanagement.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User createUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    void disableUser(Long id);

    void activateUser(Long id);

    void enrollCourse(Long userId, Long courseId);

    void unenrollCourse(Long userId, Long courseId);

    Set<Course> getEnrolledCourses(Long userId);
}
