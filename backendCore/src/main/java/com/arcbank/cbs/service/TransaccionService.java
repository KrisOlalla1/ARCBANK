package com.arcbank.cbs.service;

import com.arcbank.cbs.dto.*;
import com.arcbank.cbs.exception.*;
import com.arcbank.cbs.model.passive.CuentaAhorro;
import com.arcbank.cbs.model.passive.Transaccion;
import com.arcbank.cbs.repository.CuentaAhorroRepository;
import com.arcbank.cbs.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final CuentaAhorroRepository cuentaAhorroRepository;
    private final TransaccionRepository transaccionRepository;

    @Transactional
    public TransaccionResponse depositar(DepositoRequest request) {
        log.info("Procesando depósito en cuenta: {} por monto: {}", 
                 request.getNumeroCuenta(), request.getMonto());

        if (request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransaccionInvalidaException("El monto debe ser mayor a cero");
        }

        CuentaAhorro cuenta = cuentaAhorroRepository.findByNumeroCuenta(request.getNumeroCuenta())
                .orElseThrow(() -> new CuentaNoEncontradaException(request.getNumeroCuenta()));

        if (!"ACTIVA".equals(cuenta.getEstado())) {
            throw new CuentaInactivaException(cuenta.getNumeroCuenta(), cuenta.getEstado());
        }

        BigDecimal nuevoSaldo = cuenta.getSaldoActual().add(request.getMonto());
        cuenta.setSaldoActual(nuevoSaldo);
        cuenta.setSaldoDisponible(nuevoSaldo);
        cuenta.setFechaUltimaTransaccion(LocalDateTime.now());

        Transaccion transaccion = new Transaccion();
        transaccion.setCuentaAhorro(cuenta);
        transaccion.setTipo("DEPOSITO");
        transaccion.setMonto(request.getMonto());
        transaccion.setBalance(nuevoSaldo);
        transaccion.setFechaHora(LocalDateTime.now());
        transaccion.setDescripcion(request.getDescripcion());
        transaccion.setReferencia(normalizarReferencia(request.getReferencia(), "DEP"));
        if (request.getIdSucursalTx() != null) {

        }
        transaccion.setEstado("COMPLETADA");

        cuentaAhorroRepository.save(cuenta);
        transaccion = transaccionRepository.save(transaccion);

        log.info("Depósito completado. ID Transacción: {}, Nuevo saldo: {}", 
                 transaccion.getIdTransaccion(), nuevoSaldo);

        return mapToResponse(transaccion, cuenta.getNumeroCuenta(), "Depósito realizado exitosamente");
    }

    @Transactional
    public TransaccionResponse retirar(RetiroRequest request) {
        log.info("Procesando retiro en cuenta: {} por monto: {}", 
                 request.getNumeroCuenta(), request.getMonto());

        if (request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransaccionInvalidaException("El monto debe ser mayor a cero");
        }

        CuentaAhorro cuenta = cuentaAhorroRepository.findByNumeroCuenta(request.getNumeroCuenta())
                .orElseThrow(() -> new CuentaNoEncontradaException(request.getNumeroCuenta()));

        if (!"ACTIVA".equals(cuenta.getEstado())) {
            throw new CuentaInactivaException(cuenta.getNumeroCuenta(), cuenta.getEstado());
        }

        if (cuenta.getSaldoDisponible().compareTo(request.getMonto()) < 0) {
            throw new SaldoInsuficienteException(cuenta.getNumeroCuenta());
        }

        BigDecimal nuevoSaldo = cuenta.getSaldoActual().subtract(request.getMonto());
        cuenta.setSaldoActual(nuevoSaldo);
        cuenta.setSaldoDisponible(nuevoSaldo);
        cuenta.setFechaUltimaTransaccion(LocalDateTime.now());

        Transaccion transaccion = new Transaccion();
        transaccion.setCuentaAhorro(cuenta);
        transaccion.setTipo("RETIRO");
        transaccion.setMonto(request.getMonto());
        transaccion.setBalance(nuevoSaldo);
        transaccion.setFechaHora(LocalDateTime.now());
        transaccion.setDescripcion(request.getDescripcion());
        transaccion.setReferencia(normalizarReferencia(request.getReferencia(), "RET"));
        if (request.getIdSucursalTx() != null) {

        }
        transaccion.setEstado("COMPLETADA");

        cuentaAhorroRepository.save(cuenta);
        transaccion = transaccionRepository.save(transaccion);

        log.info("Retiro completado. ID Transacción: {}, Nuevo saldo: {}", 
                 transaccion.getIdTransaccion(), nuevoSaldo);

        return mapToResponse(transaccion, cuenta.getNumeroCuenta(), "Retiro realizado exitosamente");
    }

    @Transactional
    public TransaccionResponse transferir(TransferenciaRequest request) {
        log.info("Procesando transferencia de {} a {} por monto: {}", 
                 request.getNumeroCuentaOrigen(), 
                 request.getNumeroCuentaDestino(), 
                 request.getMonto());

        if (request.getNumeroCuentaOrigen().equals(request.getNumeroCuentaDestino())) {
            throw new TransaccionInvalidaException("No se puede transferir a la misma cuenta");
        }

        if (request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransaccionInvalidaException("El monto debe ser mayor a cero");
        }

        CuentaAhorro cuentaOrigen = cuentaAhorroRepository
                .findByNumeroCuenta(request.getNumeroCuentaOrigen())
                .orElseThrow(() -> new CuentaNoEncontradaException(request.getNumeroCuentaOrigen()));

        CuentaAhorro cuentaDestino = cuentaAhorroRepository
                .findByNumeroCuenta(request.getNumeroCuentaDestino())
                .orElseThrow(() -> new CuentaNoEncontradaException(request.getNumeroCuentaDestino()));

        if (!"ACTIVA".equals(cuentaOrigen.getEstado())) {
            throw new CuentaInactivaException(cuentaOrigen.getNumeroCuenta(), cuentaOrigen.getEstado());
        }
        if (!"ACTIVA".equals(cuentaDestino.getEstado())) {
            throw new CuentaInactivaException(cuentaDestino.getNumeroCuenta(), cuentaDestino.getEstado());
        }

        if (cuentaOrigen.getSaldoDisponible().compareTo(request.getMonto()) < 0) {
            throw new SaldoInsuficienteException(cuentaOrigen.getNumeroCuenta());
        }

        BigDecimal nuevoSaldoOrigen = cuentaOrigen.getSaldoActual().subtract(request.getMonto());
        cuentaOrigen.setSaldoActual(nuevoSaldoOrigen);
        cuentaOrigen.setSaldoDisponible(nuevoSaldoOrigen);
        cuentaOrigen.setFechaUltimaTransaccion(LocalDateTime.now());

        BigDecimal nuevoSaldoDestino = cuentaDestino.getSaldoActual().add(request.getMonto());
        cuentaDestino.setSaldoActual(nuevoSaldoDestino);
        cuentaDestino.setSaldoDisponible(nuevoSaldoDestino);
        cuentaDestino.setFechaUltimaTransaccion(LocalDateTime.now());

        Transaccion txOrigen = new Transaccion();
        txOrigen.setCuentaAhorro(cuentaOrigen);
        txOrigen.setTipo("RETIRO");
        txOrigen.setMonto(request.getMonto());
        txOrigen.setBalance(nuevoSaldoOrigen);
        txOrigen.setFechaHora(LocalDateTime.now());
        txOrigen.setDescripcion("Transferencia a cuenta " + request.getNumeroCuentaDestino() + 
                               (request.getDescripcion() != null ? " - " + request.getDescripcion() : ""));
        txOrigen.setReferencia(normalizarReferencia(request.getReferencia(), "TRF"));
        if (request.getIdSucursalTx() != null) {

        }
        txOrigen.setEstado("COMPLETADA");

        Transaccion txDestino = new Transaccion();
        txDestino.setCuentaAhorro(cuentaDestino);
        txDestino.setTipo("DEPOSITO");
        txDestino.setMonto(request.getMonto());
        txDestino.setBalance(nuevoSaldoDestino);
        txDestino.setFechaHora(LocalDateTime.now());
        txDestino.setDescripcion("Transferencia desde cuenta " + request.getNumeroCuentaOrigen() + 
                                (request.getDescripcion() != null ? " - " + request.getDescripcion() : ""));
        txDestino.setReferencia(txOrigen.getReferencia());
        if (request.getIdSucursalTx() != null) {

        }
        txDestino.setEstado("COMPLETADA");

        cuentaAhorroRepository.save(cuentaOrigen);
        cuentaAhorroRepository.save(cuentaDestino);
        txOrigen = transaccionRepository.save(txOrigen);
        transaccionRepository.save(txDestino);

        log.info("Transferencia completada. ID Transacción origen: {}", txOrigen.getIdTransaccion());

        return mapToResponse(txOrigen, cuentaOrigen.getNumeroCuenta(), "Transferencia realizada exitosamente");
    }

    private TransaccionResponse mapToResponse(Transaccion transaccion, String numeroCuenta, String mensaje) {
        TransaccionResponse response = new TransaccionResponse();
        response.setIdTransaccion(transaccion.getIdTransaccion());
        response.setNumeroCuenta(numeroCuenta);
        response.setTipo(transaccion.getTipo());
        response.setMonto(transaccion.getMonto());
        response.setBalance(transaccion.getBalance());
        response.setFechaHora(transaccion.getFechaHora());
        response.setDescripcion(transaccion.getDescripcion());
        response.setReferencia(transaccion.getReferencia());
        response.setEstado(transaccion.getEstado());
        response.setMensaje(mensaje);
        return response;
    }

    private String normalizarReferencia(String referencia, String prefijo) {
        if (referencia != null && !referencia.isBlank()) return referencia;
        String ts = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
                .format(java.time.LocalDateTime.now());
        String rand = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return prefijo + "-" + ts + "-" + rand;
    }
}
