package com.roadguard.monitoring.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtPayload {
    private String iss;
    private String sub;
    private String role;
    private String driverId;
    private long exp;
}
