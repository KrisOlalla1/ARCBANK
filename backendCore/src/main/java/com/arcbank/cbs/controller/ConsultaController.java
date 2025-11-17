package com.arcbank.cbs.controller;

import com.arcbank.cbs.dto.ClienteValidacionResponse;
import com.arcbank.cbs.dto.CuentaDetalleResponse;
import com.arcbank.cbs.dto.MovimientosResponse;
import com.arcbank.cbs.dto.PosicionConsolidadaResponse;
import com.arcbank.cbs.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/core/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @GetMapping("/cliente/{identificacion}/validar")
    public ResponseEntity<ClienteValidacionResponse> validarCliente(@PathVariable String identificacion) {
        log.info("API: Validando cliente: {}", identificacion);
        ClienteValidacionResponse response = consultaService.validarCliente(identificacion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posicion-consolidada/{identificacion}")
    public ResponseEntity<PosicionConsolidadaResponse> obtenerPosicionConsolidada(
            @PathVariable String identificacion) {
        log.info("API: Consultando posición consolidada para cliente: {}", identificacion);
        PosicionConsolidadaResponse response = consultaService.obtenerPosicionConsolidada(identificacion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/movimientos/{numeroCuenta}")
    public ResponseEntity<MovimientosResponse> obtenerMovimientos(
            @PathVariable String numeroCuenta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        
        log.info("API: Consultando movimientos para cuenta: {} desde {} hasta {}", 
                 numeroCuenta, fechaInicio, fechaFin);
        
        MovimientosResponse response = consultaService.obtenerMovimientos(numeroCuenta, fechaInicio, fechaFin);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    public ResponseEntity<CuentaDetalleResponse> obtenerDetalleCuenta(@PathVariable String numeroCuenta) {
        log.info("API: Consultando detalles de cuenta: {}", numeroCuenta);
        CuentaDetalleResponse response = consultaService.obtenerDetalleCuenta(numeroCuenta);
        return ResponseEntity.ok(response);
    }
}
