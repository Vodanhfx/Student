package com.lms.studentmanagement.dto.Exam;

import lombok.Data;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String content;
    private List<AnswerDTO> answers;
    private String type;
}