package com.roadguard.monitoring.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EndTripRequest(UUID driverId,
                             LocalDateTime startTime,
                             LocalDateTime endTime,
                             List<Break> breaks) {
}
