package com.roadguard.monitoring.service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EndTripRequest(LocalDateTime startTime,
                             LocalDateTime endTime,
                             List<Break> breaks) {
}
