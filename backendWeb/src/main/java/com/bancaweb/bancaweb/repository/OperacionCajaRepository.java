package com.bancaweb.bancaweb.repository;

import com.bancaweb.bancaweb.model.OperacionCaja;
import com.bancaweb.bancaweb.model.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperacionCajaRepository extends JpaRepository<OperacionCaja, Integer> {
    List<OperacionCaja> findByUsuarioAndFechaOperacionBetween(
            UsuarioSistema usuario,
            LocalDateTime desde,
            LocalDateTime hasta
    );
}
