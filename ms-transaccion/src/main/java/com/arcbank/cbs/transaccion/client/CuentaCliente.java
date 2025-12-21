package com.arcbank.cbs.transaccion.client;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.arcbank.cbs.transaccion.dto.SaldoDTO;

// "name" debe coincidir con el nombre de la aplicación en Eureka o Docker
// "url" toma el valor de tu application.yml
@FeignClient(name = "ms-cuentas", url = "${app.feign.cuentas-url:http://localhost:8081}")
public interface CuentaCliente {

    // 1. Obtener saldo (GET)
    // Ruta ajustada a: /api/v1/cuentas/ahorros/{id}/saldo
    @GetMapping("/api/v1/cuentas/ahorros/{id}/saldo")
    BigDecimal obtenerSaldo(@PathVariable("id") Integer id);

    // 2. Actualizar saldo (PUT) - usa SaldoDTO para serialización JSON correcta
    // Ruta ajustada a: /api/v1/cuentas/ahorros/{id}/saldo
    @PutMapping("/api/v1/cuentas/ahorros/{id}/saldo")
    void actualizarSaldo(@PathVariable("id") Integer id, @RequestBody SaldoDTO saldoDTO);
}