package com.arcbank.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCajeroDTO {
    private Integer idCajero;
    private String usuario;
    private String nombreCompleto;
    private Integer idSucursal;
    private String estado;
}