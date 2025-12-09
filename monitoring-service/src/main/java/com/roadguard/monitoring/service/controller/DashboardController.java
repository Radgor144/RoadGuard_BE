package com.roadguard.monitoring.service.controller;

import com.roadguard.monitoring.service.dto.DashboardDataResponse;
import com.roadguard.monitoring.service.dto.EarRangeRequest;
import com.roadguard.monitoring.service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping("/ear-data")
    public DashboardDataResponse getEarData(@AuthenticationPrincipal Jwt jwt, @RequestBody EarRangeRequest request) {
        UUID driverId = UUID.fromString(jwt.getClaim("driverId"));
        return dashboardService.getEarData(driverId,
                                           request.startTime(),
                                           request.endTime());
    }
}