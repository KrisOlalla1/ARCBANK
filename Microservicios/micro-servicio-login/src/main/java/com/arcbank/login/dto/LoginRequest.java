package com.arcbank.login.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String usuario;
    private String clave;
    private String tipoLogin; 
}