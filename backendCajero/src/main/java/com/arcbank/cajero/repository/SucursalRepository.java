package com.arcbank.cajero.repository;

import com.arcbank.cajero.admin.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
    Optional<Sucursal> findByCodigoUnico(String codigoUnico);
}
