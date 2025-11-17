package com.arcbank.cbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arcbank.cbs.model.core.EntidadBancaria;

/**
 * Repositorio para la entidad EntidadBancaria.
 */
@Repository
public interface EntidadBancariaRepository extends JpaRepository<EntidadBancaria, Integer> {
    Optional<EntidadBancaria> findByRuc(String ruc);
}
