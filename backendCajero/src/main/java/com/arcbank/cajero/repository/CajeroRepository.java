package com.arcbank.cajero.repository;

import com.arcbank.cajero.admin.model.Cajero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CajeroRepository extends JpaRepository<Cajero, Integer> {
    Optional<Cajero> findByUsuario(String usuario);
}
