package com.roadguard.monitoring.service.repository;

import com.roadguard.monitoring.service.entity.BreakEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BreakEntityRepository extends JpaRepository<BreakEntity, UUID> {
    List<BreakEntity> findByTrip_DriverIdAndStartTimeBetween(UUID driverId, LocalDateTime startTime, LocalDateTime endTime);
}
