package com.sqlmurdermystery.casecontent.service;

import com.sqlmurdermystery.casecontent.dto.*;
import com.sqlmurdermystery.casecontent.model.*;
import com.sqlmurdermystery.casecontent.repository.QuizAttemptRepository;
import com.sqlmurdermystery.casecontent.repository.QuizRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuizService {

    /** A learner needs at least 70% of the total points to "pass" a quiz. */
    private static final double PASS_THRESHOLD = 0.70;

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    public QuizService(QuizRepository quizRepository, QuizAttemptRepository quizAttemptRepository) {
        this.quizRepository = quizRepository;
        this.quizAttemptRepository = quizAttemptRepository;
    }

    @Transactional(readOnly = true)
    public List<QuizSummaryDto> listQuizzes() {
        return quizRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(q -> new QuizSummaryDto(
                        q.getId(),
                        q.getTopic(),
                        q.getDescription(),
                        q.getDifficulty().name(),
                        q.getTimeLimitSeconds(),
                        q.getQuestions().size()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public QuizDetailDto getQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz " + quizId + " not found"));
        return toDetailDto(quiz);
    }

    private QuizDetailDto toDetailDto(Quiz quiz) {
        List<QuizQuestionDto> questionDtos = quiz.getQuestions().stream()
                .map(q -> new QuizQuestionDto(
                        q.getId(),
                        q.getQuestionText(),
                        q.getCodeSnippet(),
                        q.getPoints(),
                        q.getOptions().stream()
                                .map(o -> new OptionDto(o.getId(), o.getOptionText()))
                                .toList()
                ))
                .toList();

        return new QuizDetailDto(
                quiz.getId(), quiz.getTopic(), quiz.getDescription(),
                quiz.getDifficulty().name(), quiz.getTimeLimitSeconds(), questionDtos
        );
    }

    /**
     * Grades a submitted attempt against the stored correct answers, persists the
     * attempt + per-question breakdown, and returns the result. Time-limit enforcement
     * here is advisory (the client-side timer is authoritative for UX); we simply cap
     * the recorded time at the quiz's limit so late submissions can't inflate scoring context.
     */
    @Transactional
    public AttemptResultDto submitAttempt(String username, Long quizId, SubmitAttemptRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz " + quizId + " not found"));

        Map<Long, QuizQuestion> questionsById = new HashMap<>();
        for (QuizQuestion q : quiz.getQuestions()) {
            questionsById.put(q.getId(), q);
        }

        QuizAttempt attempt = new QuizAttempt();
        attempt.setUsername(username);
        attempt.setQuiz(quiz);
        attempt.setTotalQuestions(quiz.getQuestions().size());

        int totalPoints = quiz.getQuestions().stream().mapToInt(QuizQuestion::getPoints).sum();
        int earnedPoints = 0;
        int correctCount = 0;

        List<QuestionResultDto> breakdown = new java.util.ArrayList<>();

        for (AnswerSubmission answer : request.getAnswers()) {
            QuizQuestion question = questionsById.get(answer.getQuestionId());
            if (question == null) continue; // ignore answers for questions not in this quiz

            QuizOption selected = question.getOptions().stream()
                    .filter(o -> o.getId().equals(answer.getSelectedOptionId()))
                    .findFirst()
                    .orElse(null);

            QuizOption correctOption = question.getOptions().stream()
                    .filter(QuizOption::isCorrect)
                    .findFirst()
                    .orElse(null);

            boolean isCorrect = selected != null && selected.isCorrect();
            if (isCorrect) {
                earnedPoints += question.getPoints();
                correctCount++;
            }

            QuizAttemptAnswer attemptAnswer = new QuizAttemptAnswer(attempt, question, selected, isCorrect);
            attempt.getAnswers().add(attemptAnswer);

            breakdown.add(new QuestionResultDto(
                    question.getId(),
                    isCorrect,
                    selected != null ? selected.getId() : null,
                    correctOption != null ? correctOption.getId() : null
            ));
        }

        boolean passed = totalPoints > 0 && ((double) earnedPoints / totalPoints) >= PASS_THRESHOLD;
        int cappedTime = request.getTimeTakenSeconds() == null
                ? 0
                : Math.min(request.getTimeTakenSeconds(), quiz.getTimeLimitSeconds());

        attempt.setScore(earnedPoints);
        attempt.setCorrectCount(correctCount);
        attempt.setTimeTakenSeconds(cappedTime);
        attempt.setPassed(passed);

        quizAttemptRepository.save(attempt);

        // TODO: publish a "quiz.completed" event / call progress-tracking-service so
        // overall course progress and the leaderboard update. See progress-tracking-service
        // and leaderboard-service skeletons for where this would land.

        return new AttemptResultDto(
                attempt.getId(), quiz.getId(), earnedPoints, attempt.getTotalQuestions(),
                correctCount, passed, cappedTime, breakdown
        );
    }

    @Transactional(readOnly = true)
    public List<AttemptResultDto> getAttemptHistory(String username, Long quizId) {
        return quizAttemptRepository.findByUsernameAndQuizIdOrderByCompletedAtDesc(username, quizId).stream()
                .map(a -> new AttemptResultDto(
                        a.getId(), a.getQuiz().getId(), a.getScore(), a.getTotalQuestions(),
                        a.getCorrectCount(), a.isPassed(), a.getTimeTakenSeconds(), List.of()
                ))
                .toList();
    }
}
