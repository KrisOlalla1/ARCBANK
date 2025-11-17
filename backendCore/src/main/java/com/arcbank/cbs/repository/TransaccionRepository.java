package com.arcbank.cbs.repository;

import com.arcbank.cbs.model.passive.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {

    List<Transaccion> findByIdCuentaOrderByFechaHoraDesc(Integer idCuenta);

    @Query("SELECT t FROM Transaccion t WHERE t.idCuenta = :idCuenta " +
           "AND t.fechaHora BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY t.fechaHora DESC")
    List<Transaccion> findByIdCuentaAndFechaBetween(
            @Param("idCuenta") Integer idCuenta,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT t FROM Transaccion t WHERE t.idCuenta = :idCuenta " +
           "AND t.estado = 'COMPLETADA' " +
           "ORDER BY t.fechaHora DESC")
    List<Transaccion> findTransaccionesCompletadasByCuenta(@Param("idCuenta") Integer idCuenta);
}
