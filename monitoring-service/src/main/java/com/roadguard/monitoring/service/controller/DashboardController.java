package com.roadguard.monitoring.service.controller;

import com.roadguard.monitoring.service.dto.EarDataResponse;
import com.roadguard.monitoring.service.dto.EarRangeRequest;
import com.roadguard.monitoring.service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping("/ear-data")
    public List<EarDataResponse> getEarData(@RequestBody EarRangeRequest request) {
        return dashboardService.getEarData(request.driverId(),
                                           request.startTime(),
                                           request.endTime());
    }
}
