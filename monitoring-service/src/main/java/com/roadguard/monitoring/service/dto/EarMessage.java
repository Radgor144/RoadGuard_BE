package com.roadguard.monitoring.service.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EarMessage(@NotNull UUID driverId,
                         @DecimalMin("0.0") @DecimalMax("1.0") Double ear,
                         long timestamp) {
}
