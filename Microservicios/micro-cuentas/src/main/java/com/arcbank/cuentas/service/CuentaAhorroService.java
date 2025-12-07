package com.arcbank.cuentas.service;

import com.arcbank.cuentas.dto.CuentaAhorroDTO;

import java.util.List;

public interface CuentaAhorroService {

    CuentaAhorroDTO crear(CuentaAhorroDTO dto);

    CuentaAhorroDTO obtenerPorId(Integer id);

    CuentaAhorroDTO obtenerPorNumeroCuenta(String numero);

    List<CuentaAhorroDTO> listarPorCliente(Integer idCliente);

    CuentaAhorroDTO actualizarEstado(Integer id, String nuevoEstado);

    CuentaAhorroDTO actualizarSaldos(Integer id, Double saldoActual, Double saldoDisponible);
}
