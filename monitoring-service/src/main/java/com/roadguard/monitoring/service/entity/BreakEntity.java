package com.roadguard.monitoring.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BreakEntity {
    @Id
    private UUID id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    private TripEntity trip;
}
