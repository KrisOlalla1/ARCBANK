package com.arcbank.cbs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteValidacionResponse {
    
    private boolean existe;
    private Integer idCliente;
    private String identificacion;
    private String tipoCliente; // P o E
    private String nombreCompleto;
    private String estado;
    private String mensaje;

    public static ClienteValidacionResponse noEncontrado(String identificacion) {
        ClienteValidacionResponse response = new ClienteValidacionResponse();
        response.setExiste(false);
        response.setIdentificacion(identificacion);
        response.setMensaje("Cliente no encontrado");
        return response;
    }
    
    public static ClienteValidacionResponse encontrado(Integer idCliente, String identificacion, 
                                                       String tipoCliente, String nombreCompleto, String estado) {
        ClienteValidacionResponse response = new ClienteValidacionResponse();
        response.setExiste(true);
        response.setIdCliente(idCliente);
        response.setIdentificacion(identificacion);
        response.setTipoCliente(tipoCliente);
        response.setNombreCompleto(nombreCompleto);
        response.setEstado(estado);
        response.setMensaje("Cliente encontrado");
        return response;
    }
}
