package com.arcbank.cuentas.controller;

import com.arcbank.cuentas.dto.CuentaAhorroDTO;
import com.arcbank.cuentas.service.CuentaAhorroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaAhorroController {

    private final CuentaAhorroService service;

    public CuentaAhorroController(CuentaAhorroService service) {
        this.service = service;
    }

    // Crear cuenta
    @PostMapping
    public ResponseEntity<CuentaAhorroDTO> create(@RequestBody CuentaAhorroDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    // Obtener cuenta por ID
    @GetMapping("/{id}")
    public ResponseEntity<CuentaAhorroDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Listar todas las cuentas
    @GetMapping
    public ResponseEntity<List<CuentaAhorroDTO>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    // Actualizar cuenta
    @PutMapping("/{id}")
    public ResponseEntity<CuentaAhorroDTO> update(@PathVariable Integer id, @RequestBody CuentaAhorroDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // Eliminar cuenta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Consultar saldo
    @GetMapping("/{id}/saldo")
    public ResponseEntity<java.math.BigDecimal> saldo(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getSaldo(id));
    }
}
