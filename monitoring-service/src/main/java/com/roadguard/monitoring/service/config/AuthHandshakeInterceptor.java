package com.roadguard.monitoring.service.config;

import com.roadguard.monitoring.service.dto.JwtPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private static final String ACCESS_TOKEN_PARAM = "access_token";
    private static final String USER_TOKEN_ATTRIBUTE = "userToken";
    private static final String DRIVER_ID_ATTRIBUTE = "driverId";

    private final JwtDecoder jwtDecoder;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        System.out.println("Headers: " + request.getHeaders());
        System.out.println("Query params: " + request.getURI().getQuery());

        String token = extractTokenFromRequest(request);
        if (token != null && token.isEmpty()) {
            log.warn("Token is empty or invalid");
            return false;
        }

        Optional<JwtPayload> jwtPayloadOpt = jwtDecoder.decode(token);
        if (jwtPayloadOpt.isEmpty()) {
            log.warn("Token is invalid");
            return false;
        }

        JwtPayload jwtPayload = jwtPayloadOpt.get();

        attributes.put(USER_TOKEN_ATTRIBUTE, token);

        if (jwtPayload.getDriverId() != null) {
            attributes.put(DRIVER_ID_ATTRIBUTE, jwtPayload.getDriverId());
        }

        log.info("Websocket handshake accepted for driverId: {}", jwtPayload.getDriverId());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            log.error("Websocket handshake failed: {}", exception.getMessage(), exception);
        }
    }

    private String extractTokenFromRequest(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest serverHttpRequest) {
            return serverHttpRequest.getServletRequest().getParameter(ACCESS_TOKEN_PARAM);
        }

        String query = request.getURI().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && ACCESS_TOKEN_PARAM.equals(keyValue[0])) {
                    return keyValue[1];
                }
            }
        }
        return null;
    }
}
