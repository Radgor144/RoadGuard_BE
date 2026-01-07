package com.roadguard.monitoring.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EarData {
    @Id
    private UUID id;
    private UUID driverId;
    private double averageEar;
    private LocalDateTime timestamp;
}
