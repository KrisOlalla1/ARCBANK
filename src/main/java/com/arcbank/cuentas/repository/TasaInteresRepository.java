package com.arcbank.cuentas.repository;

import com.arcbank.cuentas.model.TasaInteresHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasaInteresRepository extends JpaRepository<TasaInteresHistorial, Integer> {
}