package com.bancaweb.bancaweb.controller;

import com.bancaweb.bancaweb.dto.CoreDtos;
import com.bancaweb.bancaweb.dto.CuentaCoreDTO;
import com.bancaweb.bancaweb.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;


    @GetMapping("/consolidada/{identificacion}")
    public ResponseEntity<?> consultarPosicionConsolidada(@PathVariable String identificacion) {
        try {
            List<CuentaCoreDTO> cuentas = cuentaService.obtenerPosicionConsolidada(identificacion);

            if (cuentas.isEmpty()) {
                return ResponseEntity.status(204).body("No se encontraron cuentas activas para el cliente.");
            }

            return ResponseEntity.ok(cuentas);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/movimientos/{numeroCuenta}")
    public ResponseEntity<?> consultarMovimientos(
            @PathVariable String numeroCuenta,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {
        try {
            CoreDtos.MovimientosResponse resp = cuentaService.obtenerMovimientos(numeroCuenta, fechaInicio, fechaFin);
            return ResponseEntity.ok(resp);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}