package com.arcbank.cbs.transaccion.service;

import java.util.List;

import com.arcbank.cbs.transaccion.dto.TransaccionRequestDTO;
import com.arcbank.cbs.transaccion.dto.TransaccionResponseDTO;

public interface TransaccionService {

    /**
     * Procesa una nueva transacción financiera (Depósito, Retiro, Transferencia).
     * Valida saldos y actualiza cuentas remotas.
     */
    TransaccionResponseDTO crearTransaccion(TransaccionRequestDTO request);

    /**
     * Obtiene el historial de transacciones donde la cuenta participó 
     * como Origen O como Destino.
     */
    List<TransaccionResponseDTO> obtenerPorCuenta(Integer idCuenta);

    /**
     * Busca una transacción específica por su ID (Para ver recibos/detalles).
     */
    TransaccionResponseDTO obtenerPorId(Integer id);
}