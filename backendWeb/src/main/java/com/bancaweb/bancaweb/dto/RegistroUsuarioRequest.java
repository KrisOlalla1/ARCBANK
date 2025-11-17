package com.bancaweb.bancaweb.dto;

import lombok.Data;

@Data
public class RegistroUsuarioRequest {
    private String nombreUsuario;
    private String clave;
    private String tipoIdentificacion;
    private String identificacion;
    private Integer idSucursal;
}
