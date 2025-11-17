package com.arcbank.cajero.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Value("${core.api.base}")
    private String coreApiBase;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<?> getCuentaPorNumero(@PathVariable String numeroCuenta) {
        log.info("Consultando detalles de cuenta {} via Gateway", numeroCuenta);
        
        try {
            String url = coreApiBase + "/api/core/consultas/cuenta/" + numeroCuenta;
            log.info("Llamando a Core via Gateway: {}", url);
            
            ResponseEntity<?> response = restTemplate.getForEntity(url, Object.class);
            return ResponseEntity.ok(response.getBody());
            
        } catch (Exception e) {
            log.error("Error consultando cuenta {}: {}", numeroCuenta, e.getMessage());
            return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
                put("numeroCuenta", numeroCuenta);
                put("existe", false);
            }});
        }
    }
}
