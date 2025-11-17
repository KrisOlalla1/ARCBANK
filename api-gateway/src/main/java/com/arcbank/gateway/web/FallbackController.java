package com.arcbank.gateway.web;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/{service}")
    public ResponseEntity<Map<String, Object>> fallback(@PathVariable String service) {
        Map<String, Object> payload = Map.of(
            "service", service,
            "timestamp", Instant.now().toString(),
            "message", "El servicio " + service + " no está disponible temporalmente."
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(payload);
    }
}
