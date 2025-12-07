package com.arcbank.cuentas.repository;

import com.arcbank.cuentas.models.CuentaAhorro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CuentaAhorroRepository extends JpaRepository<CuentaAhorro, Integer> {

    Optional<CuentaAhorro> findByNumeroCuenta(String numeroCuenta);

    List<CuentaAhorro> findByIdCliente(Integer idCliente);
}
