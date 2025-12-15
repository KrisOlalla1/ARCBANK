package com.arcbank.cuentas.service;

import com.arcbank.cuentas.dto.request.CuentaAhorroRequest;
import com.arcbank.cuentas.dto.response.CuentaAhorroDTO;
import com.arcbank.cuentas.exception.ResourceNotFoundException;
import com.arcbank.cuentas.model.CuentaAhorro;
import com.arcbank.cuentas.model.TipoCuentaAhorro;
import com.arcbank.cuentas.repository.CuentaAhorroRepository;
import com.arcbank.cuentas.repository.TipoCuentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CuentaAhorroService {

    private final CuentaAhorroRepository cuentaRepo;
    private final TipoCuentaRepository tipoRepo;

    @Transactional
    public CuentaAhorroDTO create(CuentaAhorroRequest request) {
        TipoCuentaAhorro tipo = tipoRepo.findById(request.getIdTipoCuenta())
                .orElseThrow(() -> new ResourceNotFoundException("TipoCuenta no encontrada: " + request.getIdTipoCuenta()));

        CuentaAhorro c = new CuentaAhorro();
        c.setNumeroCuenta(request.getNumeroCuenta());
        c.setIdCliente(request.getIdCliente());
        c.setIdSucursalApertura(request.getIdSucursalApertura());
        c.setTipoCuenta(tipo);
        c.setSaldoActual(request.getSaldoInicial());
        c.setSaldoDisponible(request.getSaldoInicial());
        c.setFechaApertura(java.time.LocalDate.now());
        c.setEstado("ACTIVO");

        CuentaAhorro saved = cuentaRepo.save(c);
        log.info("Cuenta creada: {}", saved.getNumeroCuenta());
        return toDTO(saved);
    }

    public CuentaAhorroDTO findById(Integer id) {
        return cuentaRepo.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada: " + id));
    }

    public List<CuentaAhorroDTO> findAll() {
        return cuentaRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public CuentaAhorroDTO deposit(String numeroCuenta, BigDecimal amount) {
        CuentaAhorro c = cuentaRepo.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada: " + numeroCuenta));
        c.setSaldoActual(c.getSaldoActual().add(amount));
        c.setSaldoDisponible(c.getSaldoDisponible().add(amount));
        c.setFechaUltimaTransaccion(LocalDateTime.now());
        log.info("Depósito {} a cuenta {}", amount, numeroCuenta);
        return toDTO(cuentaRepo.save(c));
    }

    @Transactional
    public CuentaAhorroDTO withdraw(String numeroCuenta, BigDecimal amount) {
        CuentaAhorro c = cuentaRepo.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada: " + numeroCuenta));
        if (c.getSaldoDisponible().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Fondos insuficientes");
        }
        c.setSaldoActual(c.getSaldoActual().subtract(amount));
        c.setSaldoDisponible(c.getSaldoDisponible().subtract(amount));
        c.setFechaUltimaTransaccion(LocalDateTime.now());
        log.info("Retiro {} de cuenta {}", amount, numeroCuenta);
        return toDTO(cuentaRepo.save(c));
    }

    @Transactional
    public CuentaAhorroDTO update(Integer id, CuentaAhorroRequest request) {
        CuentaAhorro c = cuentaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada: " + id));

        TipoCuentaAhorro tipo = tipoRepo.findById(request.getIdTipoCuenta())
                .orElseThrow(() -> new ResourceNotFoundException("TipoCuenta no encontrada: " + request.getIdTipoCuenta()));

        c.setNumeroCuenta(request.getNumeroCuenta());
        c.setIdCliente(request.getIdCliente());
        c.setIdSucursalApertura(request.getIdSucursalApertura());
        c.setTipoCuenta(tipo);

        log.info("Cuenta actualizada: {}", id);
        return toDTO(cuentaRepo.save(c));
    }

    public BigDecimal getSaldo(Integer id) {
        return cuentaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada: " + id))
                .getSaldoActual();
    }

    @Transactional
    public void delete(Integer id) {
        CuentaAhorro c = cuentaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada: " + id));
        cuentaRepo.delete(c);
        log.info("Cuenta eliminada: {}", id);
    }

    private CuentaAhorroDTO toDTO(CuentaAhorro c) {
        CuentaAhorroDTO dto = new CuentaAhorroDTO();
        dto.setIdCuenta(c.getIdCuenta());
        dto.setNumeroCuenta(c.getNumeroCuenta());
        dto.setIdCliente(c.getIdCliente());
        dto.setIdSucursalApertura(c.getIdSucursalApertura());
        dto.setIdTipoCuenta(c.getTipoCuenta().getIdTipoCuenta());
        dto.setSaldoActual(c.getSaldoActual());
        dto.setSaldoDisponible(c.getSaldoDisponible());
        dto.setFechaApertura(c.getFechaApertura());
        dto.setFechaUltimaTransaccion(c.getFechaUltimaTransaccion());
        dto.setEstado(c.getEstado());
        return dto;
    }
}
