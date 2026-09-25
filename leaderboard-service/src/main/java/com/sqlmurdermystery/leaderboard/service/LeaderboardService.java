package com.sqlmurdermystery.leaderboard.service;

import com.sqlmurdermystery.leaderboard.config.LeaderboardProperties;
import com.sqlmurdermystery.leaderboard.dto.LeaderboardEntryDto;
import com.sqlmurdermystery.leaderboard.dto.MyRankDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A Redis sorted set (ZSET) is a natural fit for "real-time leaderboard": ZINCRBY is
 * O(log N), and ZREVRANGE/ZREVRANK give top-N and a user's rank without scanning
 * every row, which is the whole point of reaching for Redis instead of a SQL table
 * with an ORDER BY + COUNT(*) on every request.
 */
@Service
public class LeaderboardService {

    private final StringRedisTemplate redisTemplate;
    private final String key;

    public LeaderboardService(StringRedisTemplate redisTemplate, LeaderboardProperties properties) {
        this.redisTemplate = redisTemplate;
        this.key = properties.getRedisKey();
    }

    public void addScore(String username, int points) {
        redisTemplate.opsForZSet().incrementScore(key, username, points);
    }

    public List<LeaderboardEntryDto> getTop(int limit) {
        Set<ZSetOperations.TypedTuple<String>> tuples =
                redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, limit - 1);

        List<LeaderboardEntryDto> result = new ArrayList<>();
        if (tuples == null) return result;

        int rank = 1;
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            long score = tuple.getScore() == null ? 0 : tuple.getScore().longValue();
            result.add(new LeaderboardEntryDto(rank++, tuple.getValue(), score));
        }
        return result;
    }

    public MyRankDto getMyRank(String username) {
        Double score = redisTemplate.opsForZSet().score(key, username);
        Long zeroBasedRank = redisTemplate.opsForZSet().reverseRank(key, username);

        long safeScore = score == null ? 0 : score.longValue();
        Integer rank = zeroBasedRank == null ? null : (int) (zeroBasedRank + 1);

        return new MyRankDto(username, safeScore, rank);
    }
}
