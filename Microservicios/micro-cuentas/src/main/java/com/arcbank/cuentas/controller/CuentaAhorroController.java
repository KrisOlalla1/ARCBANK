package com.arcbank.cuentas.controller;

import com.arcbank.cuentas.dto.CuentaAhorroDTO;
import com.arcbank.cuentas.service.CuentaAhorroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaAhorroController {

    private final CuentaAhorroService service;

    @PostMapping
    public ResponseEntity<CuentaAhorroDTO> crear(@RequestBody CuentaAhorroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping("/{id}")
    public CuentaAhorroDTO obtenerPorId(@PathVariable Integer id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/numero/{numero}")
    public CuentaAhorroDTO obtenerPorNumeroCuenta(@PathVariable String numero) {
        return service.obtenerPorNumeroCuenta(numero);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<CuentaAhorroDTO> listarPorCliente(@PathVariable Integer idCliente) {
        return service.listarPorCliente(idCliente);
    }

    @PatchMapping("/{id}/estado")
    public CuentaAhorroDTO actualizarEstado(
            @PathVariable Integer id,
            @RequestParam String estado) {
        return service.actualizarEstado(id, estado);
    }

    @PatchMapping("/{id}/saldos")
    public CuentaAhorroDTO actualizarSaldos(
            @PathVariable Integer id,
            @RequestParam(required = false) Double saldoActual,
            @RequestParam(required = false) Double saldoDisponible) {
        return service.actualizarSaldos(id, saldoActual, saldoDisponible);
    }
}
