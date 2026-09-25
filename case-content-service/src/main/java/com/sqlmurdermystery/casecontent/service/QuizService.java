package com.sqlmurdermystery.casecontent.service;

import com.sqlmurdermystery.casecontent.dto.*;
import com.sqlmurdermystery.casecontent.model.Question;
import com.sqlmurdermystery.casecontent.model.QuizAttempt;
import com.sqlmurdermystery.casecontent.model.QuizAttemptAnswer;
import com.sqlmurdermystery.casecontent.model.Topic;
import com.sqlmurdermystery.casecontent.repository.QuestionRepository;
import com.sqlmurdermystery.casecontent.repository.QuizAttemptRepository;
import com.sqlmurdermystery.casecontent.repository.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Learning-phase quiz content is NOT seeded locally. It is read straight out of
 * Aiven's shared `topics` and `questions` tables (mapped by {@link Topic} and
 * {@link Question}) every time a learner opens the Learning Phase — this is the
 * only place in the service that touches those two tables. Only case metadata
 * (case_files) and this app's own attempt-history tables are owned locally.
 *
 * The Aiven `questions` table has no per-question point value or per-topic
 * difficulty/time-limit column, so those are derived here with fixed defaults
 * rather than fabricated in the shared schema. Adjust the constants below (or
 * swap in a lookup keyed by topic name) if you want per-topic tuning later.
 */
@Service
public class QuizService {

    /** A learner needs at least 70% correct to "pass" a topic's quiz. */
    private static final double PASS_THRESHOLD = 0.70;

    /** Points awarded per correct answer. */
    private static final int POINTS_PER_QUESTION = 10;

    /** Seconds allotted per question, used to derive each quiz's time limit. */
    private static final int SECONDS_PER_QUESTION = 45;

    private static final String DEFAULT_DIFFICULTY = "BEGINNER";

    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    public QuizService(TopicRepository topicRepository,
                        QuestionRepository questionRepository,
                        QuizAttemptRepository quizAttemptRepository) {
        this.topicRepository = topicRepository;
        this.questionRepository = questionRepository;
        this.quizAttemptRepository = quizAttemptRepository;
    }

    @Transactional(readOnly = true)
    public List<QuizSummaryDto> listQuizzes() {
        return topicRepository.findAllByOrderByIdAsc().stream()
                .map(topic -> {
                    int questionCount = questionRepository.findByTopicIdOrderByIdAsc(topic.getId()).size();
                    return new QuizSummaryDto(
                            topic.getId(),
                            topic.getName(),
                            "Practice questions on " + topic.getName() + ".",
                            DEFAULT_DIFFICULTY,
                            questionCount * SECONDS_PER_QUESTION,
                            questionCount
                    );
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public QuizDetailDto getQuiz(Long topicId) {
        Topic topic = findTopicOrThrow(topicId);
        List<Question> questions = questionRepository.findByTopicIdOrderByIdAsc(topicId);

        List<QuizQuestionDto> questionDtos = questions.stream()
                .map(q -> new QuizQuestionDto(q.getId(), q.getQuestion(), null, POINTS_PER_QUESTION, toOptions(q)))
                .toList();

        return new QuizDetailDto(
                topic.getId(),
                topic.getName(),
                "Practice questions on " + topic.getName() + ".",
                DEFAULT_DIFFICULTY,
                questions.size() * SECONDS_PER_QUESTION,
                questionDtos
        );
    }

    /**
     * Grades a submitted attempt against Aiven's `questions.correct_option`, persists
     * the attempt + per-question breakdown locally, and returns the result.
     */
    @Transactional
    public AttemptResultDto submitAttempt(String username, Long topicId, SubmitAttemptRequest request) {
        Topic topic = findTopicOrThrow(topicId);
        List<Question> questions = questionRepository.findByTopicIdOrderByIdAsc(topicId);

        Map<Long, Question> questionsById = new HashMap<>();
        for (Question q : questions) {
            questionsById.put(q.getId(), q);
        }

        QuizAttempt attempt = new QuizAttempt();
        attempt.setUsername(username);
        attempt.setTopicId(topic.getId());
        attempt.setTotalQuestions(questions.size());

        int correctCount = 0;
        List<QuestionResultDto> breakdown = new ArrayList<>();

        for (AnswerSubmission answer : request.getAnswers()) {
            Question question = questionsById.get(answer.getQuestionId());
            if (question == null) continue; // ignore answers for questions not in this topic

            Long correctOptionId = question.getCorrectOption() == null
                    ? null : Long.valueOf(question.getCorrectOption());

            boolean isCorrect = answer.getSelectedOptionId() != null
                    && answer.getSelectedOptionId().equals(correctOptionId);

            if (isCorrect) correctCount++;

            attempt.getAnswers().add(new QuizAttemptAnswer(
                    attempt, question.getId(), answer.getSelectedOptionId(), isCorrect
            ));

            breakdown.add(new QuestionResultDto(
                    question.getId(), isCorrect, answer.getSelectedOptionId(), correctOptionId
            ));
        }

        int totalPoints = questions.size() * POINTS_PER_QUESTION;
        int earnedPoints = correctCount * POINTS_PER_QUESTION;
        boolean passed = totalPoints > 0 && ((double) earnedPoints / totalPoints) >= PASS_THRESHOLD;

        int quizTimeLimit = questions.size() * SECONDS_PER_QUESTION;
        int cappedTime = request.getTimeTakenSeconds() == null
                ? 0
                : Math.min(request.getTimeTakenSeconds(), quizTimeLimit);

        attempt.setScore(earnedPoints);
        attempt.setCorrectCount(correctCount);
        attempt.setTimeTakenSeconds(cappedTime);
        attempt.setPassed(passed);

        quizAttemptRepository.save(attempt);

        // TODO: publish a "quiz.completed" event / call progress-tracking-service so
        // overall course progress and the leaderboard update.

        return new AttemptResultDto(
                attempt.getId(), topic.getId(), earnedPoints, attempt.getTotalQuestions(),
                correctCount, passed, cappedTime, breakdown
        );
    }

    @Transactional(readOnly = true)
    public List<AttemptResultDto> getAttemptHistory(String username, Long topicId) {
        return quizAttemptRepository.findByUsernameAndTopicIdOrderByCompletedAtDesc(username, topicId).stream()
                .map(a -> new AttemptResultDto(
                        a.getId(), a.getTopicId(), a.getScore(), a.getTotalQuestions(),
                        a.getCorrectCount(), a.isPassed(), a.getTimeTakenSeconds(), List.of()
                ))
                .toList();
    }

    private Topic findTopicOrThrow(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz " + topicId + " not found"));
    }

    /** Aiven's `questions` table stores options as flat columns, not rows — option "id" is its 1-4 slot. */
    private List<OptionDto> toOptions(Question q) {
        List<OptionDto> options = new ArrayList<>(4);
        if (q.getOption1() != null) options.add(new OptionDto(1L, q.getOption1()));
        if (q.getOption2() != null) options.add(new OptionDto(2L, q.getOption2()));
        if (q.getOption3() != null) options.add(new OptionDto(3L, q.getOption3()));
        if (q.getOption4() != null) options.add(new OptionDto(4L, q.getOption4()));
        return options;
    }
}
