package com.roadguard.monitoring.service.service;

import com.roadguard.monitoring.service.dto.BreakResponse;
import com.roadguard.monitoring.service.dto.DashboardDataResponse;
import com.roadguard.monitoring.service.dto.EarDataResponse;
import com.roadguard.monitoring.service.entity.BreakEntity;
import com.roadguard.monitoring.service.entity.EarData;
import com.roadguard.monitoring.service.repository.BreakEntityRepository;
import com.roadguard.monitoring.service.repository.EarDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final EarDataRepository earDataRepository;
    private final BreakEntityRepository breakEntityRepository;
    private static final int MAX_POINTS = 250;

    public DashboardDataResponse getEarData(UUID driverId, LocalDateTime startTime, LocalDateTime endTime) {
        List<EarData> allData = earDataRepository.findByDriverIdAndTimestampBetweenOrderByTimestampAsc(driverId, startTime, endTime);
        List<BreakEntity> breaks = breakEntityRepository.findByTrip_DriverIdAndStartTimeBetween(driverId, startTime, endTime);

        List<BreakResponse> breakResponses = mapBreaksToResponses(breaks);

        List<EarData> activeData = allData.stream()
                .filter(earData -> !isDuringBreak(earData.getTimestamp(), breaks))
                .toList();

        List<EarDataResponse> reducedActiveData = reduceAndAverageData(activeData);

        return new DashboardDataResponse(reducedActiveData, breakResponses);
    }

    private List<EarDataResponse> reduceAndAverageData(List<EarData> activeData) {
        int totalPoints = activeData.size();
        if (totalPoints == 0) return List.of();

        if (totalPoints <= MAX_POINTS) {
            return activeData.stream().map(data -> new EarDataResponse(data.getTimestamp(), data.getAverageEar()))
                    .toList();
        }

        int groupSize = (int) Math.ceil((double) totalPoints / MAX_POINTS);
        List<EarDataResponse> reduced = new ArrayList<>();

        for (int i = 0; i < totalPoints; i += groupSize) {
            int end = Math.min(i + groupSize, totalPoints);
            List<EarData> group = activeData.subList(i, end);
            double avg = group.stream().mapToDouble(EarData::getAverageEar).average().orElse(Double.NaN);
            LocalDateTime timestamp = group.get(group.size() / 2).getTimestamp();
            reduced.add(new EarDataResponse(timestamp, avg));
        }
        return reduced;
    }

    private boolean isDuringBreak(LocalDateTime timestamp, List<BreakEntity> breaks) {
        return breaks.stream()
                .anyMatch(breakEntity -> (timestamp.isEqual(breakEntity.getStartTime()) || timestamp.isAfter(breakEntity.getStartTime())) &&
                        (timestamp.isBefore(breakEntity.getEndTime()) || timestamp.isEqual(breakEntity.getEndTime())));
    }

    private List<BreakResponse> mapBreaksToResponses(List<BreakEntity> breaks) {
        return breaks.stream()
                .map(b -> new BreakResponse(
                        b.getStartTime(),
                        b.getEndTime(),
                        Duration.between(b.getStartTime(), b.getEndTime()).toMinutes()
                ))
                .toList();
    }
}