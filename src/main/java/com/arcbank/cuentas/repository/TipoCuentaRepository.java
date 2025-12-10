package com.arcbank.cuentas.repository;


import com.arcbank.cuentas.model.TipoCuentaAhorro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoCuentaRepository extends JpaRepository<TipoCuentaAhorro, Integer> {
}