package com.arcbank.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUsuarioSistemaDTO {
    private Integer idUsuario;
    private String nombreUsuario;
    private Integer idSucursal;
    private String estado;
}