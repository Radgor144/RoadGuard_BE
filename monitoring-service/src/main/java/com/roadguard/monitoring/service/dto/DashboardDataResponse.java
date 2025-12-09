package com.roadguard.monitoring.service.dto;

import java.util.List;

public record DashboardDataResponse(
        List<EarDataResponse> activeDriveData,
        List<BreakResponse> breaks
) {}
