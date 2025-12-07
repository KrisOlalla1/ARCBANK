package com.arcbank.login.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Object userInfo; // Contendrá LoginCajeroDTO o LoginUsuarioSistemaDTO
    private String mensaje;
    private boolean autenticado;
    private String tipoUsuario; // Ej: "CAJERO" o "SISTEMA"
}