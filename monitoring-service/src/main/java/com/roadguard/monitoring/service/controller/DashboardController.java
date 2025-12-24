package com.roadguard.monitoring.service.controller;

import com.roadguard.monitoring.service.dto.DashboardDataResponse;
import com.roadguard.monitoring.service.dto.DrivingSessionTime;
import com.roadguard.monitoring.service.dto.EarRangeRequest;
import com.roadguard.monitoring.service.service.DashboardService;
import com.roadguard.monitoring.service.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final TimeService timeService;

    @PostMapping("/ear-data")
    public DashboardDataResponse getEarData(@AuthenticationPrincipal Jwt jwt, @RequestBody EarRangeRequest request) {
        UUID driverId = getDriverId(jwt);
        return dashboardService.getEarData(driverId,
                                           request.startTime(),
                                           request.endTime());
    }

    @GetMapping("/driving-sessions")
    public List<DrivingSessionTime> getTripTimeRanges(@AuthenticationPrincipal Jwt jwt) {
        UUID driverId = getDriverId(jwt);
        return timeService.getTripTimeRanges(driverId);
    }

    private UUID getDriverId(Jwt jwt) {
        UUID driverId = UUID.fromString(jwt.getClaim("driverId"));
        return driverId;
    }
}