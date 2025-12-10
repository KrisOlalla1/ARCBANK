package com.arcbank.cuentas.service;

import com.arcbank.cuentas.dto.CuentaAhorroDTO;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaAhorroService {

    CuentaAhorroDTO create(CuentaAhorroDTO dto);

    CuentaAhorroDTO findById(Integer id);

    CuentaAhorroDTO findByNumeroCuenta(String numeroCuenta);

    List<CuentaAhorroDTO> findAll();

    CuentaAhorroDTO deposit(String numeroCuenta, BigDecimal amount);

    CuentaAhorroDTO withdraw(String numeroCuenta, BigDecimal amount);

    CuentaAhorroDTO update(Integer id, CuentaAhorroDTO dto); // agregado

    BigDecimal getSaldo(Integer id); // agregado

    void delete(Integer id);
}
