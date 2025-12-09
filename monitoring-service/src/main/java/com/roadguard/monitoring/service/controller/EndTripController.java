package com.roadguard.monitoring.service.controller;

import com.roadguard.monitoring.service.dto.EndTripRequest;
import com.roadguard.monitoring.service.service.EndTripService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/endTrip")
@RequiredArgsConstructor
public class EndTripController {
    private final EndTripService endTripService;

    @PostMapping
    public void endTrip(@AuthenticationPrincipal Jwt jwt, @RequestBody EndTripRequest endTripRequest) {
        UUID driverId = UUID.fromString(jwt.getClaim("driverId"));
        endTripService.endTrip(driverId, endTripRequest);
    }
}
