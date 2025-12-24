package com.roadguard.monitoring.service.service;

import com.roadguard.monitoring.service.dto.DrivingSessionTime;
import com.roadguard.monitoring.service.repository.EndTripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimeService {
    private final EndTripRepository endTripRepository;

    public List<DrivingSessionTime> getTripTimeRanges(UUID driverId) {
        return endTripRepository.findAll().stream()
                .filter(trip -> trip.getDriverId().equals(driverId))
                .map(trip -> new DrivingSessionTime(trip.getStartTime(), trip.getEndTime()))
                .toList();
    }
}
