package com.roadguard.monitoring.service.dto;

import java.time.LocalDateTime;

public record EarRangeRequest(LocalDateTime startTime,
                              LocalDateTime endTime) {
}
