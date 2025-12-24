package com.roadguard.monitoring.service.dto;

import java.time.LocalDateTime;

public record DrivingSessionTime(LocalDateTime startTime,
                                 LocalDateTime endTime) {

}
