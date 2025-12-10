package com.roadguard.monitoring.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadguard.monitoring.service.dto.JwtPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtDecoder {
    private final ObjectMapper objectMapper;

    public Optional<JwtPayload> decode(String accessToken) {
        Optional<String> encodedPayload = getEncodedPayload(accessToken);
        if (encodedPayload.isPresent()) {
            Optional<String> decodedPayload = decodePayload(encodedPayload.get());
            if (decodedPayload.isPresent()) {
                return parsePayload(decodedPayload.get());
            }
        }
        return empty();
    }

    private Optional<String> getEncodedPayload(String accessToken) {
        try {
            List<String> splitList = List.of(accessToken.split("\\."));
            if (splitList.size() == 3) {
                return of(splitList.get(1));
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            log.warn("Token parsing error: {}", exception);
        }
        return empty();
    }

    private Optional<JwtPayload> parsePayload(String decodedPayload) {
        try {
            return ofNullable(objectMapper.readValue(decodedPayload, JwtPayload.class));
        } catch (Exception exception) {
            log.warn("Payload parsing error: {}", decodedPayload, exception);
        }
        return empty();
    }

    private Optional<String> decodePayload(String encodedPayload) {
        try {
            return of(byteArrayToString(Base64.getUrlDecoder().decode(encodedPayload)));
        } catch (IllegalArgumentException exception) {
            log.warn("Base64 decoding error {}", exception);
        }
        return empty();
    }

    private static String byteArrayToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
