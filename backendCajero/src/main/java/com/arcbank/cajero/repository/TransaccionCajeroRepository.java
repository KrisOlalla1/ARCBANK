package com.arcbank.cajero.repository;

import com.arcbank.cajero.accounts.model.TransaccionCajero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransaccionCajeroRepository extends JpaRepository<TransaccionCajero, Integer> {
    List<TransaccionCajero> findByEstado(String estado);
    List<TransaccionCajero> findByNumeroCuentaOrderByFechaHoraDesc(String numeroCuenta);
}
