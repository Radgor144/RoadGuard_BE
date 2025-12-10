package com.roadguard.monitoring.service.config;

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
    private static final String DRIVER_ID_ATTRIBUTE = "driverId";

    private final JwtDecoder jwtDecoder;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        return extractToken(request)
                .flatMap(jwtDecoder::decode)
                .map(jwtPayload -> {
                    Optional.ofNullable(jwtPayload.getDriverId())
                            .ifPresent(driverId -> attributes.put(DRIVER_ID_ATTRIBUTE, driverId));

                    log.info("WebSocket handshake accepted for driverId: {}", jwtPayload.getDriverId());
                    return true;
                })
                .orElseGet(() -> {
                    log.warn("WebSocket handshake rejected: Token missing or invalid.");
                    return false;
                });
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            log.error("WebSocket handshake failed", exception);
        }
    }

    private Optional<String> extractToken(ServerHttpRequest request) {
        String token = null;

        if (request instanceof ServletServerHttpRequest serverReq) {
            token = serverReq.getServletRequest().getParameter(ACCESS_TOKEN_PARAM);
        }

        if (token == null) {
            String query = request.getURI().getQuery();
            if (query != null) {
                for (String param : query.split("&")) {
                    String[] kv = param.split("=");
                    if (kv.length == 2 && ACCESS_TOKEN_PARAM.equals(kv[0])) {
                        token = kv[1];
                        break;
                    }
                }
            }
        }
        return Optional.ofNullable(token)
                .filter(t -> !t.isEmpty());
    }
}
