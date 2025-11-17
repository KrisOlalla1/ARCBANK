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


    public List<CuentaCoreDTO> obtenerPosicionConsolidada(String identificacionCliente) {
        return coreClientService.obtenerPosicionConsolidadaPorIdentificacion(identificacionCliente);
    }

    public CoreDtos.MovimientosResponse obtenerMovimientos(String numeroCuenta, String fechaInicio, String fechaFin) {
        return coreClientService.obtenerMovimientosPorCuenta(numeroCuenta, fechaInicio, fechaFin);
    }
}