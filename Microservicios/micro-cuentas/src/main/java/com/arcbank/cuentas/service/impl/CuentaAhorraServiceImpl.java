package com.arcbank.cuentas.service.impl;

import com.arcbank.cuentas.dto.CuentaAhorroDTO;
import com.arcbank.cuentas.exception.ResourceNotFoundException;
import com.arcbank.cuentas.models.CuentaAhorro;
import com.arcbank.cuentas.models.TipoCuentaAhorro;
import com.arcbank.cuentas.repository.CuentaAhorroRepository;
import com.arcbank.cuentas.repository.TipoCuentaAhorroRepository;
import com.arcbank.cuentas.service.CuentaAhorroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuentaAhorroServiceImpl implements CuentaAhorroService {

    private final CuentaAhorroRepository cuentaRepo;
    private final TipoCuentaAhorroRepository tipoRepo;

    @Override
    public CuentaAhorroDTO crear(CuentaAhorroDTO dto) {
        TipoCuentaAhorro tipo = tipoRepo.findById(dto.getIdTipoCuenta())
                .orElseThrow(() -> new ResourceNotFoundException("TipoCuentaAhorro no encontrado: " + dto.getIdTipoCuenta()));

        CuentaAhorro cuenta = CuentaAhorro.builder()
                .numeroCuenta(dto.getNumeroCuenta())
                .idCliente(dto.getIdCliente())
                .idSucursalApertura(dto.getIdSucursalApertura())
                .tipoCuenta(tipo)
                .saldoActual(dto.getSaldoActual() != null ? dto.getSaldoActual() : 0.0)
                .saldoDisponible(dto.getSaldoDisponible() != null ? dto.getSaldoDisponible() : 0.0)
                .fechaApertura(dto.getFechaApertura() != null ? dto.getFechaApertura() : LocalDate.now())
                .fechaUltimaTransaccion(dto.getFechaUltimaTransaccion())
                .estado(dto.getEstado() != null ? dto.getEstado() : "ACTIVA")
                .build();

        return toDto(cuentaRepo.save(cuenta));
    }

    @Override
    public CuentaAhorroDTO obtenerPorId(Integer id) {
        return cuentaRepo.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("CuentaAhorro no encontrada: " + id));
    }

    @Override
    public CuentaAhorroDTO obtenerPorNumeroCuenta(String numero) {
        return cuentaRepo.findByNumeroCuenta(numero)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("CuentaAhorro no encontrada: " + numero));
    }

    @Override
    public List<CuentaAhorroDTO> listarPorCliente(Integer idCliente) {
        return cuentaRepo.findByIdCliente(idCliente)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaAhorroDTO actualizarEstado(Integer id, String nuevoEstado) {
        CuentaAhorro cuenta = cuentaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CuentaAhorro no encontrada: " + id));
        cuenta.setEstado(nuevoEstado);
        return toDto(cuentaRepo.save(cuenta));
    }

    @Override
    public CuentaAhorroDTO actualizarSaldos(Integer id, Double saldoActual, Double saldoDisponible) {
        CuentaAhorro cuenta = cuentaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CuentaAhorro no encontrada: " + id));
        if (saldoActual != null) cuenta.setSaldoActual(saldoActual);
        if (saldoDisponible != null) cuenta.setSaldoDisponible(saldoDisponible);
        return toDto(cuentaRepo.save(cuenta));
    }

    // ====== Mapper ======
    private CuentaAhorroDTO toDto(CuentaAhorro c) {
        return CuentaAhorroDTO.builder()
                .idCuenta(c.getIdCuenta())
                .numeroCuenta(c.getNumeroCuenta())
                .idCliente(c.getIdCliente())
                .idSucursalApertura(c.getIdSucursalApertura())
                .idTipoCuenta(c.getTipoCuenta().getIdTipoCuenta())
                .saldoActual(c.getSaldoActual())
                .saldoDisponible(c.getSaldoDisponible())
                .fechaApertura(c.getFechaApertura())
                .fechaUltimaTransaccion(c.getFechaUltimaTransaccion())
                .estado(c.getEstado())
                .build();
    }
}
