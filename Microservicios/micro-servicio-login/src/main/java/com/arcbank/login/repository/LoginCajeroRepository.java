package com.arcbank.login.repository;

import com.arcbank.login.model.LoginCajero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginCajeroRepository extends JpaRepository<LoginCajero, Integer> {
    // Puedes agregar métodos personalizados si son necesarios
}