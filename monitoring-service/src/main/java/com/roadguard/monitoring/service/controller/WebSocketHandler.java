package com.roadguard.monitoring.service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadguard.monitoring.service.service.EarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private static final String DRIVER_ID_ATTRIBUTE = "driverId";
    private final Map<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();
    private final EarService earService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        String driverId = (String) session.getAttributes().get(DRIVER_ID_ATTRIBUTE);

        log.info("New connection established. Session ID: {}, driverId: {}", sessionId, driverId);
        activeSessions.put(sessionId, session);

        session.sendMessage(new TextMessage(String.format(
                "{\"type\":\"WELCOME\",\"message\":\"Connection established successfully.\",\"sessionId\":\"%s\"}",
                sessionId
        )));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String driverId = Optional.ofNullable((String) session.getAttributes().get(DRIVER_ID_ATTRIBUTE))
                .orElseThrow(() -> new IllegalStateException("driverId not found in WebSocket session attributes"));

        try {
            processMessage(driverId, message.getPayload());
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            session.sendMessage(new TextMessage(String.format(
                    "{\"type\":\"ERROR\",\"message\":\"%s\"}", e.getMessage()
            )));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String sessionId = session.getId();
        log.info("Connection closed. Session ID: {}, Close Status: {}", sessionId, status);
        activeSessions.remove(sessionId);
    }

    private void processMessage(String driverId, String payload) throws Exception {
        JsonNode node = objectMapper.readTree(payload);
        if (node.has("ear")) {
            double earValue = node.get("ear").asDouble();
            earService.addEarValue(UUID.fromString(driverId), earValue);
            log.info("Added EAR value {} for driverId {}", earValue, driverId);
        }
    }
}
