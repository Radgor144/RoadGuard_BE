package com.roadguard.monitoring.service.controller;

import com.roadguard.monitoring.service.dto.EarMessage;
import com.roadguard.monitoring.service.service.EarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class EarDataController {

    private final EarService earService;

    @MessageMapping("/ear-data")
    public void handleEarMessage(@Valid EarMessage message) {
        log.info("Received EAR message from client. Value: {}", message.ear());
        earService.addEarValue(message.driverId(), message.ear());
    }
}