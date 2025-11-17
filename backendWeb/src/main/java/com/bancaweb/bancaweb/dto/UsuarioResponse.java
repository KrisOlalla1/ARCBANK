package com.bancaweb.bancaweb.dto;

import com.bancaweb.bancaweb.model.UsuarioSistema;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UsuarioResponse {
    private Integer idUsuario;
    private String nombreUsuario;
    private String identificacion;
    private Integer idSucursal;

    public UsuarioResponse(UsuarioSistema usuario, String identificacion) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.identificacion = identificacion;
        this.idSucursal = usuario.getIdSucursal();
    }
}
