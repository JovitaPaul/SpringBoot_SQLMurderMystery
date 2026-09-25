package com.sqlmurdermystery.progress.controller;

import com.sqlmurdermystery.progress.dto.ProgressSummaryDto;
import com.sqlmurdermystery.progress.dto.RecordCompletionRequest;
import com.sqlmurdermystery.progress.service.ProgressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    /**
     * Called by the frontend right after a quiz is passed or a case accusation comes
     * back correct. This is the single place a learner's progress gets written.
     */
    @PostMapping("/events")
    public ResponseEntity<ProgressSummaryDto> recordCompletion(@Valid @RequestBody RecordCompletionRequest request,
                                                                 Authentication authentication,
                                                                 @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(progressService.recordCompletion(authentication.getName(), request, authorizationHeader));
    }

    /** Dashboard summary for the current learner. */
    @GetMapping("/me")
    public ResponseEntity<ProgressSummaryDto> getMyProgress(Authentication authentication) {
        return ResponseEntity.ok(progressService.getSummary(authentication.getName()));
    }
}
