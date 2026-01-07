package com.roadguard.monitoring.service.service;

import com.roadguard.monitoring.service.dto.EarAccumulator;
import com.roadguard.monitoring.service.entity.EarData;
import com.roadguard.monitoring.service.repository.EarDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class EarService {

    private final EarDataRepository repository;
    private final Map<UUID, EarAccumulator> accumulators = new ConcurrentHashMap<>();
    private static final long ONE_MINUTE_MS = 60_000;

    public void addEarValue(UUID driverId, double ear) {
        accumulators.computeIfAbsent(driverId, id -> new EarAccumulator())
                    .add(ear);
    }

    @Scheduled(fixedRate = ONE_MINUTE_MS)
    public void calculateAndSaveAverage() {
        accumulators.forEach(this::saveAverage);
    }

    private void saveAverage(UUID driverId, EarAccumulator acc) {
        double average = acc.averageAndReset();
        if (Double.isNaN(average)) return;

        EarData data = new EarData(UUID.randomUUID(),
                                   driverId,
                                   average,
                                   LocalDateTime.now());

        repository.save(data);
    }
}