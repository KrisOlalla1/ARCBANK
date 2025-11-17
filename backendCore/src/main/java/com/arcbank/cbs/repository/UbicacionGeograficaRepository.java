package com.arcbank.cbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arcbank.cbs.model.geography.UbicacionGeografica;

/**
 * Repositorio para la entidad UbicacionGeografica.
 */
@Repository
public interface UbicacionGeograficaRepository extends JpaRepository<UbicacionGeografica, Integer> {
    /**
     * Busca ubicaciones por tipo (PAIS, PROVINCIA, CANTON, PARROQUIA).
     */
    List<UbicacionGeografica> findByTipo(String tipo);

    /**
     * Busca ubicaciones hijo de una ubicación padre específica.
     */
    List<UbicacionGeografica> findByUbicacionPadreIdUbicacion(Integer idUbicacionPadre);

    /**
     * Busca una ubicación por nombre.
     */
    Optional<UbicacionGeografica> findByNombre(String nombre);

    /**
     * Busca ubicaciones raíz (sin padre).
     */
    List<UbicacionGeografica> findByUbicacionPadreIsNull();
}
