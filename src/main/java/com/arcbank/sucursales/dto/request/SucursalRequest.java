package com.arcbank.sucursales.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SucursalRequest {

    @NotBlank(message = "Código único obligatorio")
    private String codigoUnico;

    @NotBlank(message = "Nombre obligatorio")
    private String nombre;

    @NotBlank(message = "Dirección obligatoria")
    private String direccion;

    @NotBlank(message = "Teléfono obligatorio")
    private String telefono;

    @NotNull(message = "Latitud obligatoria")
    private Double latitud;

    @NotNull(message = "Longitud obligatoria")
    private Double longitud;

    @NotBlank(message = "Estado obligatorio")
    private String estado;

    @NotNull(message = "Fecha de apertura obligatoria")
    private LocalDate fechaApertura;

    @NotNull
    @Valid
    private EntidadBancariaRequest entidadBancaria;

    @NotNull
    @Valid
    private UbicacionRequest ubicacion;
}
