package com.bancaweb.bancaweb.repository;

import com.bancaweb.bancaweb.model.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
import java.util.Optional;

public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistema, Integer> {
    Optional<UsuarioSistema> findByNombreUsuario(String nombreUsuario);
}
