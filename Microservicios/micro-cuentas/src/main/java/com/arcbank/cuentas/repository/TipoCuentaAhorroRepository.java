package com.arcbank.cuentas.repository;

import com.arcbank.cuentas.models.TipoCuentaAhorro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoCuentaAhorroRepository extends JpaRepository<TipoCuentaAhorro, Integer> {

    Optional<TipoCuentaAhorro> findByNombre(String nombre);
}
