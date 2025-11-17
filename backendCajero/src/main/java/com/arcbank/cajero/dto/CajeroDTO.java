package com.arcbank.cajero.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CajeroDTO {
    private Integer idCajero;
    private String usuario;
    private String nombreCompleto;
    private String sucursal;
    private String estado;
}
