package com.arcbank.cuentas.repository;

import com.arcbank.cuentas.model.CuentaAhorro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaAhorroRepository extends JpaRepository<CuentaAhorro, Integer> {
    Optional<CuentaAhorro> findByNumeroCuenta(String numeroCuenta);
}