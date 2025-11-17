package com.arcbank.cbs.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arcbank.cbs.model.geography.Feriado;


@Repository
public interface FeriadoRepository extends JpaRepository<Feriado, Integer> {

    Optional<Feriado> findByFecha(LocalDate fecha);

    List<Feriado> findByEsNacionalTrue();


    List<Feriado> findByUbicacionGeograficaIdUbicacion(Integer idUbicacion);

    List<Feriado> findByFechaBetweenAndEstado(LocalDate fechaInicio, LocalDate fechaFin, String estado);


    @Query("SELECT f FROM Feriado f WHERE f.fecha = :fecha " +
           "AND (f.esNacional = true OR f.ubicacionGeografica.idUbicacion = :idUbicacion)")
    List<Feriado> findByFechaAndUbicacion(
            @Param("fecha") LocalDate fecha,
            @Param("idUbicacion") Integer idUbicacion
    );

    boolean existsByFecha(LocalDate fecha);
}

