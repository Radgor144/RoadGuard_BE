package com.roadguard.monitoring.service.dto;

import java.time.LocalDateTime;

public record BreakResponse(
        LocalDateTime startTime,
        LocalDateTime endTime,
        long durationMinutes
) {}
