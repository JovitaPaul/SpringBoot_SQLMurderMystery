package com.sqlmurdermystery.progress.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfig {

    /** Streak days roll over at midnight in this zone. Containers default to UTC, which
     *  would end an Indian learner's "day" at 5:30 AM. Override with PROGRESS_ZONE_ID. */
    @Bean
    public Clock clock(@Value("${progress.zone-id:Asia/Kolkata}") String zoneId) {
        return Clock.system(ZoneId.of(zoneId));
    }
}
