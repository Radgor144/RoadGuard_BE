package com.roadguard.monitoring.service.dto;

import java.time.LocalDateTime;

public record Break(LocalDateTime startTime,
                    LocalDateTime endTime) {
}
