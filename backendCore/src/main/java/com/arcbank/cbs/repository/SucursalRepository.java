package com.arcbank.cbs.repository;

import com.arcbank.cbs.model.admin.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

    Optional<Sucursal> findByCodigoUnico(String codigoUnico);

    boolean existsByCodigoUnico(String codigoUnico);
}
