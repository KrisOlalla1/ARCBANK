package com.bancaweb.bancaweb.service;

import com.bancaweb.bancaweb.dto.CoreDtos;
import com.bancaweb.bancaweb.dto.CuentaCoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaService {

    @Autowired
    private CoreClientService coreClientService;

    // Asume que el IdCliente del Core se obtiene de alguna manera, por ejemplo, consultando al Core con la identificación
    // del cliente logueado (esta lógica de obtener el ID del cliente Core debería estar en UsuarioSistemaService/Controller al inicio de sesión)
    // Por ahora, simulamos que podemos obtener la identificación del usuario para buscar su ID de cliente en el Core.

    /**
     * Obtiene la posición consolidada (cuentas y saldos) de un cliente desde el Core.
     */
    public List<CuentaCoreDTO> obtenerPosicionConsolidada(String identificacionCliente) {
        // Llamar al Core por identificación (vía API) y transformar
        return coreClientService.obtenerPosicionConsolidadaPorIdentificacion(identificacionCliente);
    }

    public CoreDtos.MovimientosResponse obtenerMovimientos(String numeroCuenta, String fechaInicio, String fechaFin) {
        return coreClientService.obtenerMovimientosPorCuenta(numeroCuenta, fechaInicio, fechaFin);
    }
}