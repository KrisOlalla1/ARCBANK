package com.arcbank.cbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arcbank.cbs.model.account.TipoCuentaAhorro;

/**
 * Repositorio para la entidad TipoCuentaAhorro.
 */
@Repository
public interface TipoCuentaAhorroRepository extends JpaRepository<TipoCuentaAhorro, Integer> {
    /**
     * Busca un tipo de cuenta por su nombre.
     */
    Optional<TipoCuentaAhorro> findByNombre(String nombre);

    /**
     * Busca tipos de cuenta activos.
     */
    List<TipoCuentaAhorro> findByActivoTrue();

    /**
     * Busca tipos de cuenta inactivos.
     */
    List<TipoCuentaAhorro> findByActivoFalse();

    /**
     * Busca un tipo de cuenta específico si está activo.
     */
    Optional<TipoCuentaAhorro> findByIdTipoCuentaAndActivoTrue(Integer idTipoCuenta);
}
