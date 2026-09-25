package com.sqlmurdermystery.casecontent.controller;

import com.sqlmurdermystery.casecontent.dto.*;
import com.sqlmurdermystery.casecontent.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /** Learning Phase topic list — public, no login required to browse. */
    @GetMapping
    public ResponseEntity<List<QuizSummaryDto>> listQuizzes() {
        return ResponseEntity.ok(quizService.listQuizzes());
    }

    /** Quiz questions + options (correct answers stripped). */
    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDetailDto> getQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getQuiz(quizId));
    }

    /** Submit answers for a timed attempt. Requires auth so we know whose progress to record. */
    @PostMapping("/{quizId}/attempts")
    public ResponseEntity<AttemptResultDto> submitAttempt(@PathVariable Long quizId,@Valid @RequestBody SubmitAttemptRequest request,Authentication authentication) {
        return ResponseEntity.ok(quizService.submitAttempt(authentication.getName(), quizId, request));
    }

    /** Past attempts by the current learner for this quiz, most recent first. */
    @GetMapping("/{quizId}/attempts")
    public ResponseEntity<List<AttemptResultDto>> getAttemptHistory(@PathVariable Long quizId,
                                                                      Authentication authentication) {
        return ResponseEntity.ok(quizService.getAttemptHistory(authentication.getName(), quizId));
    }
}
