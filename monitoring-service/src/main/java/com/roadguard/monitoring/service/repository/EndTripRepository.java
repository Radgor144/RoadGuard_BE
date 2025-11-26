package com.roadguard.monitoring.service.repository;

import com.roadguard.monitoring.service.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EndTripRepository extends JpaRepository<TripEntity, UUID> {
}
