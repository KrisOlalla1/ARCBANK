package com.arcbank.cbs.repository;

import com.arcbank.cbs.model.passive.CuentaAhorro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaAhorroRepository extends JpaRepository<CuentaAhorro, Integer> {

    Optional<CuentaAhorro> findByNumeroCuenta(String numeroCuenta);

    List<CuentaAhorro> findByIdCliente(Integer idCliente);

    @Query("SELECT c FROM CuentaAhorro c WHERE c.idCliente = :idCliente AND c.estado = 'ACTIVA'")
    List<CuentaAhorro> findCuentasActivasByCliente(@Param("idCliente") Integer idCliente);

    boolean existsByNumeroCuenta(String numeroCuenta);

    @Query("SELECT c FROM CuentaAhorro c WHERE c.numeroCuenta = :numeroCuenta AND c.estado = 'ACTIVA'")
    Optional<CuentaAhorro> findCuentaActivaByNumero(@Param("numeroCuenta") String numeroCuenta);
}
