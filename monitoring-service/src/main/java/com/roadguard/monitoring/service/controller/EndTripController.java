package com.roadguard.monitoring.service.controller;

import com.roadguard.monitoring.service.dto.EndTripRequest;
import com.roadguard.monitoring.service.service.EndTripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/endTrip")
@RequiredArgsConstructor
public class EndTripController {
    private final EndTripService endTripService;

    @PostMapping
    public void endTrip(@RequestBody EndTripRequest endTripRequest) {
        endTripService.endTrip(endTripRequest);
    }
}
