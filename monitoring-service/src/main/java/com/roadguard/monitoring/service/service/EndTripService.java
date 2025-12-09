package com.roadguard.monitoring.service.service;

import com.roadguard.monitoring.service.dto.EndTripRequest;
import com.roadguard.monitoring.service.entity.BreakEntity;
import com.roadguard.monitoring.service.entity.TripEntity;
import com.roadguard.monitoring.service.repository.EndTripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EndTripService {
    private final EndTripRepository endTripRepository;

    public void endTrip(UUID driverId, EndTripRequest request) {
        TripEntity trip = mapToEntity(driverId, request);
        endTripRepository.save(trip);
    }

    private TripEntity mapToEntity(UUID driverId, EndTripRequest request) {
        TripEntity trip = TripEntity.builder().id(UUID.randomUUID())
                                              .driverId(driverId)
                                              .startTime(request.startTime())
                                              .endTime(request.endTime())
                                              .build();

        List<BreakEntity> breakEntities = request.breaks().stream()
                .map(breakDto -> BreakEntity.builder().id(UUID.randomUUID())
                                                            .startTime(breakDto.startTime())
                                                            .endTime(breakDto.endTime())
                                                            .trip(trip)
                                                            .build())
                .toList();

        trip.setBreaks(breakEntities);
        return trip;
    }
}
