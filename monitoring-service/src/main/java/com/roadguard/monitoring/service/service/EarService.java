package com.roadguard.monitoring.service.service;

import com.roadguard.monitoring.service.entity.EarData;
import com.roadguard.monitoring.service.repository.EarDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@RequiredArgsConstructor
@Service
public class EarService {
    private final EarDataRepository repository;
    private final Map<UUID, Queue<Double>> earBuffers = new ConcurrentHashMap<>();
    private static final long ONE_MINUTE_MS = 60_000;

    public void addEarValue(UUID driverId, double ear) {
        earBuffers.computeIfAbsent(driverId, k -> new ConcurrentLinkedQueue<>()).add(ear);
    }

    @Scheduled(fixedRate = ONE_MINUTE_MS)
    public void calculateAndSaveAverage() {
        earBuffers.forEach(this::saveAverage);
    }

    private void saveAverage(UUID driverId, Queue<Double> buffer) {
        double average = buffer.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(Double.NaN);

        if (Double.isNaN(average)) return;

        buffer.clear();

        EarData data = new EarData();
        data.setId(UUID.randomUUID());
        data.setDriverId(driverId);
        data.setAverageEar(average);
        data.setTimestamp(LocalDateTime.now());
        repository.save(data);
    }
}