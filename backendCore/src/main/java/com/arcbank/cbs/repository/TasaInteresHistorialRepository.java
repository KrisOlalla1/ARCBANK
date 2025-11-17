package com.arcbank.cbs.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arcbank.cbs.model.account.TasaInteresHistorial;


@Repository
public interface TasaInteresHistorialRepository extends JpaRepository<TasaInteresHistorial, Integer> {

    List<TasaInteresHistorial> findByTipoCuentaAhorroIdTipoCuenta(Integer idTipoCuenta);


    @Query("SELECT th FROM TasaInteresHistorial th " +
           "WHERE th.tipoCuentaAhorro.idTipoCuenta = :idTipoCuenta " +
           "AND th.fechaFinVigencia IS NULL")
    Optional<TasaInteresHistorial> findVigenteByIdTipoCuenta(@Param("idTipoCuenta") Integer idTipoCuenta);


    @Query("SELECT th FROM TasaInteresHistorial th " +
           "WHERE th.tipoCuentaAhorro.idTipoCuenta = :idTipoCuenta " +
           "AND th.fechaInicioVigencia <= :fecha " +
           "AND (th.fechaFinVigencia IS NULL OR th.fechaFinVigencia >= :fecha)")
    Optional<TasaInteresHistorial> findByIdTipoCuentaAndFecha(
            @Param("idTipoCuenta") Integer idTipoCuenta,
            @Param("fecha") LocalDate fecha
    );


    List<TasaInteresHistorial> findByFechaInicioVigenciaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
