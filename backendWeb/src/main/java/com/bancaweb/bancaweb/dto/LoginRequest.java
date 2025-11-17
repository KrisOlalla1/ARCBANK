package com.bancaweb.bancaweb.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String nombreUsuario;
    private String clave;
}
