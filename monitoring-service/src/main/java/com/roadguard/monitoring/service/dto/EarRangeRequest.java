package com.roadguard.monitoring.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EarRangeRequest(UUID driverId,
                              LocalDateTime startTime,
                              LocalDateTime endTime) {
}
