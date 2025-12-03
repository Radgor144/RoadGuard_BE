package com.roadguard.monitoring.service.dto;

import java.time.LocalDateTime;

public record EarDataResponse(LocalDateTime timestamp,
                              double averageEar) {
}

