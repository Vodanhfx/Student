package com.lms.studentmanagement.dto.Exam;

import lombok.Data;
import java.util.Map;

@Data
public class ExamAttemptDTO {
    private Long examId;
    private Long userId;
    // Map<QuestionId, AnswerId>
    private Map<Long, Long> questionAnswers;
}
