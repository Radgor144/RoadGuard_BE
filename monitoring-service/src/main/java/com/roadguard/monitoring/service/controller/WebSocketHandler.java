package com.roadguard.monitoring.service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadguard.monitoring.service.service.EarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private static final String USER_TOKEN_ATTRIBUTE = "userToken";
    private final Map<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();
    private final EarService earService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        log.info("New connection established. Session ID: {}", sessionId);

        activeSessions.put(sessionId, session);

        String welcomeMessage = String.format(
                "{\"type\": \"WELCOME\", \"message\": \"Connection established successfully.\", \"sessionId\": \"%s\"}",
                sessionId
        );
        session.sendMessage(new TextMessage(welcomeMessage));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = session.getId();
        String payload = message.getPayload();

        log.info("Received message from Session ID {}: {}", sessionId, payload);

        try {
            String driverId = (String) session.getAttributes().get("driverId");
            if (driverId == null) {
                throw new IllegalArgumentException("driverId not found in WebSocket session attributes");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(payload);
            if (node.has("ear")) {
                double earValue = node.get("ear").asDouble();
                earService.addEarValue(UUID.fromString(driverId), earValue);
                log.info("Added EAR value {} for driverId {}", earValue, driverId);
            }

            String response = String.format("{\"type\": \"ACK\", \"timestamp\": %d}", System.currentTimeMillis());
            session.sendMessage(new TextMessage(response));

        } catch (Exception e) {
            log.error("Error processing WebSocket message: {}", e.getMessage(), e);
            String errorResponse = String.format("{\"type\": \"ERROR\", \"message\": \"%s\"}", e.getMessage());
            session.sendMessage(new TextMessage(errorResponse));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        String sessionId = session.getId();
        log.info("Connection closed. Session ID: {}, Close Status: {}", sessionId, status);

        activeSessions.remove(sessionId);
    }

    private String processMessage(String message) {
        return String.format("{\"type\": \"ECHO\", \"message\": \"%s\", \"timestamp\": %d}", message, System.currentTimeMillis());
    }
}
