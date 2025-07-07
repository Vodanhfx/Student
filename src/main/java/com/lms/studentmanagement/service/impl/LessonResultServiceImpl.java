package com.lms.studentmanagement.service.impl;

import com.lms.studentmanagement.exception.ResourceNotFoundException;
import com.lms.studentmanagement.model.Lesson;
import com.lms.studentmanagement.model.LessonResult;
import com.lms.studentmanagement.model.User;
import com.lms.studentmanagement.repository.LessonRepository;
import com.lms.studentmanagement.repository.LessonResultRepository;
import com.lms.studentmanagement.repository.UserRepository;
import com.lms.studentmanagement.service.LessonResultService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LessonResultServiceImpl implements LessonResultService {
    private static final Logger logger = LoggerFactory.getLogger(LessonResultServiceImpl.class);
    private final LessonResultRepository lessonResultRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    public LessonResultServiceImpl(LessonResultRepository lessonResultRepository, LessonRepository lessonRepository, UserRepository userRepository) {
        this.lessonResultRepository = lessonResultRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public LessonResult submitLessonResult(Long lessonId, LessonResult result, Long studentId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found: " + lessonId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
        result.setLesson(lesson);
        result.setUser(student);
        try {
            // Simulate file save, throw if fails
            if ("fail".equals(result.getUploadedFile())) throw new RuntimeException("File save failed!");
            logger.info("Lesson result submitted for lesson: {} by student: {}", lessonId, studentId);
            return lessonResultRepository.save(result);
        } catch (Exception e) {
            logger.error("Failed to submit lesson result, rolling back.", e);
            throw e; // Triggers rollback
        }
    }

    @Override
    @Transactional
    public LessonResult reviewLessonResult(Long resultId, LessonResult.Status status, String comment, Long teacherId) {
        LessonResult lr = lessonResultRepository.findById(resultId)
                .orElseThrow(() -> new ResourceNotFoundException("LessonResult not found: " + resultId));
        lr.setStatus(status);
        lr.setComment(comment);
        logger.info("Lesson result {} reviewed by teacher {} - status: {}", resultId, teacherId, status);
        return lessonResultRepository.save(lr);
    }
}