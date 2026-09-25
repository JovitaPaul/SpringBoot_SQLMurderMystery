package com.sqlmurdermystery.leaderboard.controller;

import com.sqlmurdermystery.leaderboard.dto.LeaderboardEntryDto;
import com.sqlmurdermystery.leaderboard.dto.MyRankDto;
import com.sqlmurdermystery.leaderboard.dto.ScoreUpdateRequest;
import com.sqlmurdermystery.leaderboard.service.LeaderboardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    /** Adds points to the current learner's score. Called by progress-tracking-service
     *  with the learner's own forwarded bearer token after a quiz/case completion. */
    @PostMapping("/score")
    public ResponseEntity<Void> addScore(@Valid @RequestBody ScoreUpdateRequest request, Authentication authentication) {
        leaderboardService.addScore(authentication.getName(), request.getPoints());
        return ResponseEntity.ok().build();
    }

    /** Public — no login required to browse the leaderboard. */
    @GetMapping("/top")
    public ResponseEntity<List<LeaderboardEntryDto>> getTop(@RequestParam(defaultValue = "10") int limit) {
        int cappedLimit = Math.min(Math.max(limit, 1), 100);
        return ResponseEntity.ok(leaderboardService.getTop(cappedLimit));
    }

    @GetMapping("/me")
    public ResponseEntity<MyRankDto> getMyRank(Authentication authentication) {
        return ResponseEntity.ok(leaderboardService.getMyRank(authentication.getName()));
    }
}
