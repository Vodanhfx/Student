package com.lms.studentmanagement.service.impl;

import com.lms.studentmanagement.dto.Exam.*;
import com.lms.studentmanagement.model.Exam.*;
import com.lms.studentmanagement.model.Lesson;
import com.lms.studentmanagement.model.User;
import com.lms.studentmanagement.repository.Exam.*;
import com.lms.studentmanagement.repository.UserRepository;
import com.lms.studentmanagement.repository.LessonRepository;
import com.lms.studentmanagement.service.ExamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    public ExamServiceImpl(
            ExamRepository examRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            ExamAttemptRepository examAttemptRepository,
            UserRepository userRepository,
            LessonRepository lessonRepository
    ) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.examAttemptRepository = examAttemptRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public ExamDTO getExamForLesson(Long lessonId) {
        Exam exam = examRepository.findByLesson_Id(lessonId);
        if (exam == null) throw new RuntimeException("Exam not found for lesson: " + lessonId);

        ExamDTO dto = new ExamDTO();
        dto.setId(exam.getId());
        dto.setLessonId(lessonId);

        // Randomize questions
        List<Question> questions = new ArrayList<>(exam.getQuestions());
        Collections.shuffle(questions);

        dto.setQuestions(
                questions.stream()
                        .map(this::toQuestionDTOWithShuffledAnswers)
                        .collect(Collectors.toList())
        );
        dto.setDurationMinutes(exam.getDurationMinutes());
        dto.setLocation(exam.getLocation());
        dto.setTimeslot(exam.getTimeslot() != null ? exam.getTimeslot().toString() : null);
        return dto;
    }

    private QuestionDTO toQuestionDTOWithShuffledAnswers(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setContent(question.getContent());

        List<Answer> answers = new ArrayList<>(question.getAnswers());
        Collections.shuffle(answers);

        dto.setAnswers(answers.stream().map(this::toAnswerDTO).collect(Collectors.toList()));
        return dto;
    }

    private AnswerDTO toAnswerDTO(Answer answer) {
        AnswerDTO dto = new AnswerDTO();
        dto.setId(answer.getId());
        dto.setContent(answer.getContent());
        return dto;
    }

    @Override
    @Transactional
    public ExamResultDTO submitExamAttempt(ExamAttemptDTO attemptDTO) {
        Exam exam = examRepository.findById(attemptDTO.getExamId())
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        User user = userRepository.findById(attemptDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        int tries = examAttemptRepository.countByExamIdAndUserId(exam.getId(), user.getId());
        if (tries >= 3) throw new RuntimeException("No tries left for this exam");

        ExamAttempt attempt = new ExamAttempt();
        attempt.setExam(exam);
        attempt.setUser(user);
        attempt.setAttemptNumber(tries + 1);

        int score = 0;
        int total = exam.getQuestions().size();
        List<QuestionFeedbackDTO> feedbackList = new ArrayList<>();

        for (Question q : exam.getQuestions()) {
            Long userAnswerId = attemptDTO.getQuestionAnswers().get(q.getId());
            QuestionFeedbackDTO feedback = new QuestionFeedbackDTO();
            feedback.setQuestionId(q.getId());
            feedback.setQuestionContent(q.getContent());

            if (userAnswerId == null) {
                feedback.setCorrect(false);
                // Set correct answer info if available
                Answer correctAns = q.getAnswers().stream().filter(Answer::isCorrect).findFirst().orElse(null);
                if (correctAns != null) {
                    feedback.setCorrectAnswerId(correctAns.getId());
                    feedback.setCorrectAnswerContent(correctAns.getContent());
                }
                feedbackList.add(feedback);
                continue;
            }

            Answer answer = answerRepository.findById(userAnswerId)
                    .orElseThrow(() -> new RuntimeException("Answer not found"));
            feedback.setChosenAnswerId(answer.getId());
            feedback.setChosenAnswerContent(answer.getContent());

            if (answer.isCorrect()) {
                score++;
                feedback.setCorrect(true);
            } else {
                feedback.setCorrect(false);
                // fill correct answer info
                Answer correctAns = q.getAnswers().stream().filter(Answer::isCorrect).findFirst().orElse(null);
                if (correctAns != null) {
                    feedback.setCorrectAnswerId(correctAns.getId());
                    feedback.setCorrectAnswerContent(correctAns.getContent());
                }
            }
            feedbackList.add(feedback);
        }
        attempt.setScore(score);
        attempt.setCompleted(true);
        examAttemptRepository.save(attempt);

        ExamResultDTO result = new ExamResultDTO();
        result.setScore(score);
        result.setTotalQuestions(total);
        result.setAttemptNumber(attempt.getAttemptNumber());
        result.setCompleted(true);
        result.setQuestionFeedbacks(feedbackList);
        return result;
    }

    @Override
    public int getTriesLeft(Long lessonId, Long userId) {
        Exam exam = examRepository.findByLesson_Id(lessonId);
        if (exam == null) throw new RuntimeException("Exam not found for lesson: " + lessonId);
        int tries = examAttemptRepository.countByExamIdAndUserId(exam.getId(), userId);
        return Math.max(0, 3 - tries);
    }

    @Override
    public List<ExamResultDTO> getExamHistory(Long examId, Long userId) {
        List<ExamAttempt> attempts = examAttemptRepository.findByExamIdAndUserIdOrderByAttemptNumberAsc(examId, userId);
        return attempts.stream().map(attempt -> {
            ExamResultDTO dto = new ExamResultDTO();
            dto.setScore(attempt.getScore());
            dto.setTotalQuestions(attempt.getExam().getQuestions().size());
            dto.setAttemptNumber(attempt.getAttemptNumber());
            dto.setCompleted(attempt.isCompleted());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ExamDTO createExam(ExamCreateDTO examCreateDTO, Long teacherId) {
        Lesson lesson = lessonRepository.findById(examCreateDTO.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        if (lesson == null && examCreateDTO.getLessonId() != null) {
            throw new RuntimeException("Lesson object not provided. Please fetch Lesson by ID before calling createExam.");
        }
        if (!lesson.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("Only the teacher who created this lesson can create an exam for it.");
        }
        Exam exam = new Exam();
        exam.setLesson(lesson);
        exam.setDurationMinutes(examCreateDTO.getDurationMinutes());
        exam.setLocation(examCreateDTO.getLocation());
        if (examCreateDTO.getTimeslot() != null) {
            exam.setTimeslot(java.time.LocalDateTime.parse(examCreateDTO.getTimeslot()));
        }
        List<Question> questions = new ArrayList<>();
        if (examCreateDTO.getQuestions() != null) {
            for (QuestionCreateDTO qdto : examCreateDTO.getQuestions()) {
                Question q = new Question();
                q.setContent(qdto.getContent());
                q.setExam(exam);
                List<Answer> answers = new ArrayList<>();
                if (qdto.getAnswers() != null) {
                    for (AnswerCreateDTO adto : qdto.getAnswers()) {
                        Answer a = new Answer();
                        a.setContent(adto.getContent());
                        a.setCorrect(adto.isCorrect());
                        a.setQuestion(q);
                        answers.add(a);
                    }
                }
                q.setAnswers(answers);
                questions.add(q);
            }
        }
        exam.setQuestions(questions);
        Exam savedExam = examRepository.save(exam);
        return toExamDTO(savedExam);
    }

    @Override
    @Transactional
    public ExamDTO updateExam(Long examId, ExamCreateDTO examCreateDTO, Long teacherId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        // Check permission
        if (exam.getLesson() == null || exam.getLesson().getTeacher() == null
                || !exam.getLesson().getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("Only the teacher who created this lesson can update this exam.");
        }
        exam.setDurationMinutes(examCreateDTO.getDurationMinutes());
        exam.setLocation(examCreateDTO.getLocation());
        if (examCreateDTO.getTimeslot() != null) {
            exam.setTimeslot(java.time.LocalDateTime.parse(examCreateDTO.getTimeslot()));
        }

        // Remove old questions & answers
        if (exam.getQuestions() != null) {
            exam.getQuestions().clear();
        }
        List<Question> newQuestions = new ArrayList<>();
        if (examCreateDTO.getQuestions() != null) {
            for (QuestionCreateDTO qdto : examCreateDTO.getQuestions()) {
                Question q = new Question();
                q.setContent(qdto.getContent());
                q.setExam(exam);
                List<Answer> answers = new ArrayList<>();
                if (qdto.getAnswers() != null) {
                    for (AnswerCreateDTO adto : qdto.getAnswers()) {
                        Answer a = new Answer();
                        a.setContent(adto.getContent());
                        a.setCorrect(adto.isCorrect());
                        a.setQuestion(q);
                        answers.add(a);
                    }
                }
                q.setAnswers(answers);
                newQuestions.add(q);
            }
        }
        exam.setQuestions(newQuestions);

        Exam savedExam = examRepository.save(exam);
        return toExamDTO(savedExam);
    }

    @Override
    @Transactional
    public void deleteExam(Long examId, Long teacherId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        if (exam.getLesson() == null || exam.getLesson().getTeacher() == null
                || !exam.getLesson().getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("Only the teacher who created this lesson can delete this exam.");
        }
        examRepository.deleteById(examId);
    }

    @Override
    public List<ExamDTO> getAllExamsForTeacher(Long teacherId) {
        List<Exam> exams = examRepository.findAll().stream()
                .filter(e -> e.getLesson() != null && e.getLesson().getTeacher() != null
                        && e.getLesson().getTeacher().getId().equals(teacherId))
                .collect(Collectors.toList());
        return exams.stream().map(this::toExamDTO).collect(Collectors.toList());
    }

    // --- Helper method to map Exam to ExamDTO ---
    private ExamDTO toExamDTO(Exam exam) {
        ExamDTO dto = new ExamDTO();
        dto.setId(exam.getId());
        dto.setLessonId(exam.getLesson() != null ? exam.getLesson().getId() : null);
        dto.setDurationMinutes(exam.getDurationMinutes());
        dto.setLocation(exam.getLocation());
        dto.setTimeslot(exam.getTimeslot() != null ? exam.getTimeslot().toString() : null);
        if (exam.getQuestions() != null) {
            List<QuestionDTO> questions = exam.getQuestions().stream()
                    .map(this::toQuestionDTO)
                    .collect(Collectors.toList());
            dto.setQuestions(questions);
        }
        return dto;
    }

    private QuestionDTO toQuestionDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setContent(question.getContent());
        if (question.getAnswers() != null) {
            dto.setAnswers(question.getAnswers().stream()
                    .map(this::toAnswerDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}