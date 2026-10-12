package com.sqlmurdermystery.progress.controller;

import com.sqlmurdermystery.progress.dto.LeaderboardEntryDto;
import com.sqlmurdermystery.progress.dto.MyRankDto;
import com.sqlmurdermystery.progress.service.LeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    /** Public — no login required to browse the leaderboard. */
    @GetMapping("/top")
    public ResponseEntity<List<LeaderboardEntryDto>> getTop(@RequestParam(defaultValue = "10") int limit) {
        int capped = Math.min(Math.max(limit, 1), 100);
        return ResponseEntity.ok(leaderboardService.getTop(capped));
    }

    @GetMapping("/me")
    public ResponseEntity<MyRankDto> getMyRank(Authentication authentication) {
        return ResponseEntity.ok(leaderboardService.getMyRank(authentication.getName()));
    }
}
