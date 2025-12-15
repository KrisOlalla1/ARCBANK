package com.arcbank.cuentas.controller;

import com.arcbank.cuentas.dto.request.CuentaAhorroRequest;
import com.arcbank.cuentas.dto.response.CuentaAhorroDTO;
import com.arcbank.cuentas.service.CuentaAhorroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas/v1/cuentas-ahorro")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cuentas de Ahorro", description = "Gestión de cuentas de ahorro")
public class CuentaAhorroController {

    private final CuentaAhorroService service;

    @Operation(summary = "Crear cuenta de ahorro")
    @PostMapping
    public ResponseEntity<CuentaAhorroDTO> create(@Valid @RequestBody CuentaAhorroRequest request) {
        log.info("Creando cuenta {}", request.getNumeroCuenta());
        return ResponseEntity.ok(service.create(request));
    }

    @Operation(summary = "Obtener cuenta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CuentaAhorroDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Listar cuentas")
    @GetMapping
    public ResponseEntity<List<CuentaAhorroDTO>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Consultar saldo")
    @GetMapping("/{id}/saldo")
    public ResponseEntity<BigDecimal> saldo(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getSaldo(id));
    }
}
