package com.arcbank.cbs.controller;

import com.arcbank.cbs.dto.DepositoRequest;
import com.arcbank.cbs.dto.RetiroRequest;
import com.arcbank.cbs.dto.TransaccionResponse;
import com.arcbank.cbs.dto.TransferenciaRequest;
import com.arcbank.cbs.service.TransaccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/core/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @PostMapping("/depositar")
    public ResponseEntity<TransaccionResponse> depositar(@Valid @RequestBody DepositoRequest request) {
        log.info("API: Solicitud de depósito recibida para cuenta: {}", request.getNumeroCuenta());
        TransaccionResponse response = transaccionService.depositar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Alias para compatibilidad con frontend cajero
    @PostMapping("/deposito")
    public ResponseEntity<TransaccionResponse> deposito(@Valid @RequestBody DepositoRequest request) {
        return depositar(request);
    }

    @PostMapping("/retirar")
    public ResponseEntity<TransaccionResponse> retirar(@Valid @RequestBody RetiroRequest request) {
        log.info("API: Solicitud de retiro recibida para cuenta: {}", request.getNumeroCuenta());
        TransaccionResponse response = transaccionService.retirar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Alias para compatibilidad con frontend cajero
    @PostMapping("/retiro")
    public ResponseEntity<TransaccionResponse> retiro(@Valid @RequestBody RetiroRequest request) {
        return retirar(request);
    }

    @PostMapping("/transferir")
    public ResponseEntity<TransaccionResponse> transferir(@Valid @RequestBody TransferenciaRequest request) {
        log.info("API: Solicitud de transferencia recibida de {} a {}", 
                 request.getNumeroCuentaOrigen(), request.getNumeroCuentaDestino());
        TransaccionResponse response = transaccionService.transferir(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
