package com.arcbank.cbs.controller;

import com.arcbank.cbs.dto.DepositoRequest;
import com.arcbank.cbs.dto.RetiroRequest;
import com.arcbank.cbs.dto.TransaccionExternaRequest;
import com.arcbank.cbs.dto.TransaccionResponse;
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
public class TransaccionExternaController {

    private final TransaccionService transaccionService;

    @PostMapping
    public ResponseEntity<TransaccionResponse> procesarTransaccion(
            @Valid @RequestBody TransaccionExternaRequest request) {
        
        log.info("API External: Transacción recibida - Tipo: {}, Cuenta: {}, Monto: {}", 
                 request.getTipo(), request.getNumeroCuenta(), request.getMonto());

        TransaccionResponse response;

        if ("DEPOSITO".equalsIgnoreCase(request.getTipo())) {
            DepositoRequest depositoRequest = new DepositoRequest();
            depositoRequest.setNumeroCuenta(request.getNumeroCuenta());
            depositoRequest.setMonto(request.getMonto());
            depositoRequest.setDescripcion(request.getDescripcion());
            depositoRequest.setReferencia(request.getReferencia());
            depositoRequest.setIdSucursalTx(request.getIdSucursal());

            response = transaccionService.depositar(depositoRequest);

        } else if ("RETIRO".equalsIgnoreCase(request.getTipo())) {
            RetiroRequest retiroRequest = new RetiroRequest();
            retiroRequest.setNumeroCuenta(request.getNumeroCuenta());
            retiroRequest.setMonto(request.getMonto());
            retiroRequest.setDescripcion(request.getDescripcion());
            retiroRequest.setReferencia(request.getReferencia());
            retiroRequest.setIdSucursalTx(request.getIdSucursal());

            response = transaccionService.retirar(retiroRequest);

        } else {
            log.error("Tipo de transacción inválido: {}", request.getTipo());
            throw new IllegalArgumentException("Tipo de transacción no válido: " + request.getTipo());
        }

        log.info("Transacción procesada exitosamente. ID: {}, Balance: {}", 
                 response.getIdTransaccion(), response.getBalance());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
