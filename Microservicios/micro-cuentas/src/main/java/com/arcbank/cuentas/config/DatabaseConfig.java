package com.arcbank.cuentas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.arcbank.cuentas.repository")
public class DatabaseConfig {
    // Aquí podrías agregar proveedores adicionales, auditoría, interceptores, etc.
}
