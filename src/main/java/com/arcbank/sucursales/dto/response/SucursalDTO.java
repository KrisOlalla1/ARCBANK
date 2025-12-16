package com.arcbank.sucursales.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SucursalDTO {

    private String idSucursal;
    private String codigoUnico;
    private String nombre;
    private String direccion;
    private String telefono;
    private Double latitud;
    private Double longitud;
    private String estado;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaApertura;

    private EntidadBancariaDTO entidadBancaria;
    private UbicacionDTO ubicacion;
}
