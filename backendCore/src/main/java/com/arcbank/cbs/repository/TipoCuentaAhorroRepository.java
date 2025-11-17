package com.arcbank.cbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arcbank.cbs.model.account.TipoCuentaAhorro;


@Repository
public interface TipoCuentaAhorroRepository extends JpaRepository<TipoCuentaAhorro, Integer> {

    Optional<TipoCuentaAhorro> findByNombre(String nombre);

    List<TipoCuentaAhorro> findByActivoTrue();


    List<TipoCuentaAhorro> findByActivoFalse();


    Optional<TipoCuentaAhorro> findByIdTipoCuentaAndActivoTrue(Integer idTipoCuenta);
}
