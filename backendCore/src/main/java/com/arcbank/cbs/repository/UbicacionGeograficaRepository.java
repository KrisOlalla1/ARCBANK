package com.arcbank.cbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arcbank.cbs.model.geography.UbicacionGeografica;


@Repository
public interface UbicacionGeograficaRepository extends JpaRepository<UbicacionGeografica, Integer> {

    List<UbicacionGeografica> findByTipo(String tipo);

    List<UbicacionGeografica> findByUbicacionPadreIdUbicacion(Integer idUbicacionPadre);


    Optional<UbicacionGeografica> findByNombre(String nombre);


    List<UbicacionGeografica> findByUbicacionPadreIsNull();
}
