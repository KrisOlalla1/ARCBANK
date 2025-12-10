package com.arcbank.cuentas.service.impl;

import com.arcbank.cuentas.dto.CuentaAhorroDTO;
import com.arcbank.cuentas.model.CuentaAhorro;
import com.arcbank.cuentas.model.TipoCuentaAhorro;
import com.arcbank.cuentas.exception.ResourceNotFoundException;
import com.arcbank.cuentas.repository.CuentaAhorroRepository;
import com.arcbank.cuentas.repository.TipoCuentaRepository;
import com.arcbank.cuentas.service.CuentaAhorroService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaAhorroServiceImpl implements CuentaAhorroService {

    private final CuentaAhorroRepository cuentaRepo;
    private final TipoCuentaRepository tipoRepo;

    public CuentaAhorroServiceImpl(CuentaAhorroRepository cuentaRepo, TipoCuentaRepository tipoRepo) {
        this.cuentaRepo = cuentaRepo;
        this.tipoRepo = tipoRepo;
    }

    @Override
    public CuentaAhorroDTO create(CuentaAhorroDTO dto) {
        TipoCuentaAhorro tipo = tipoRepo.findById(dto.getIdTipoCuenta())
                .orElseThrow(() -> new ResourceNotFoundException("TipoCuenta not found: " + dto.getIdTipoCuenta()));
        CuentaAhorro e = new CuentaAhorro();
        e.setNumeroCuenta(dto.getNumeroCuenta());
        e.setIdCliente(dto.getIdCliente());
        e.setIdSucursalApertura(dto.getIdSucursalApertura());
        e.setTipoCuenta(tipo);
        e.setSaldoActual(dto.getSaldoActual());
        e.setSaldoDisponible(dto.getSaldoDisponible());
        e.setFechaApertura(dto.getFechaApertura());
        e.setFechaUltimaTransaccion(dto.getFechaUltimaTransaccion());
        e.setEstado(dto.getEstado());
        e = cuentaRepo.save(e);
        dto.setIdCuenta(e.getIdCuenta());
        return dto;
    }

    @Override
    public CuentaAhorroDTO findById(Integer id) {
        return cuentaRepo.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta not found: " + id));
    }

    @Override
    public CuentaAhorroDTO findByNumeroCuenta(String numeroCuenta) {
        return cuentaRepo.findByNumeroCuenta(numeroCuenta).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta not found: " + numeroCuenta));
    }

    @Override
    public List<CuentaAhorroDTO> findAll() {
        return cuentaRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CuentaAhorroDTO deposit(String numeroCuenta, java.math.BigDecimal amount) {
        CuentaAhorro c = cuentaRepo.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta not found: " + numeroCuenta));
        c.setSaldoActual(c.getSaldoActual().add(amount));
        c.setSaldoDisponible(c.getSaldoDisponible().add(amount));
        c.setFechaUltimaTransaccion(LocalDateTime.now());
        cuentaRepo.save(c);
        return toDto(c);
    }

    @Override
    @Transactional
    public CuentaAhorroDTO withdraw(String numeroCuenta, java.math.BigDecimal amount) {
        CuentaAhorro c = cuentaRepo.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta not found: " + numeroCuenta));
        if (c.getSaldoDisponible().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Fondos insuficientes");
        }
        c.setSaldoActual(c.getSaldoActual().subtract(amount));
        c.setSaldoDisponible(c.getSaldoDisponible().subtract(amount));
        c.setFechaUltimaTransaccion(LocalDateTime.now());
        cuentaRepo.save(c);
        return toDto(c);
    }

    @Override
    @Transactional
    public CuentaAhorroDTO update(Integer id, CuentaAhorroDTO dto) {
        CuentaAhorro c = cuentaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta not found: " + id));

        c.setNumeroCuenta(dto.getNumeroCuenta());
        c.setIdCliente(dto.getIdCliente());
        c.setIdSucursalApertura(dto.getIdSucursalApertura());

        TipoCuentaAhorro tipo = tipoRepo.findById(dto.getIdTipoCuenta())
                .orElseThrow(() -> new ResourceNotFoundException("TipoCuenta not found: " + dto.getIdTipoCuenta()));
        c.setTipoCuenta(tipo);

        c.setSaldoActual(dto.getSaldoActual());
        c.setSaldoDisponible(dto.getSaldoDisponible());
        c.setFechaApertura(dto.getFechaApertura());
        c.setFechaUltimaTransaccion(dto.getFechaUltimaTransaccion());
        c.setEstado(dto.getEstado());

        cuentaRepo.save(c);
        return toDto(c);
    }

    @Override
    public java.math.BigDecimal getSaldo(Integer id) {
        return cuentaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta not found: " + id))
                .getSaldoActual();
    }

    @Override
    public void delete(Integer id) {
        CuentaAhorro c = cuentaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta not found: " + id));
        cuentaRepo.delete(c);
    }

    private CuentaAhorroDTO toDto(CuentaAhorro e) {
        CuentaAhorroDTO d = new CuentaAhorroDTO();
        d.setIdCuenta(e.getIdCuenta());
        d.setNumeroCuenta(e.getNumeroCuenta());
        d.setIdCliente(e.getIdCliente());
        d.setIdSucursalApertura(e.getIdSucursalApertura());
        d.setIdTipoCuenta(e.getTipoCuenta().getIdTipoCuenta());
        d.setSaldoActual(e.getSaldoActual());
        d.setSaldoDisponible(e.getSaldoDisponible());
        d.setFechaApertura(e.getFechaApertura());
        d.setFechaUltimaTransaccion(e.getFechaUltimaTransaccion());
        d.setEstado(e.getEstado());
        return d;
    }
}
