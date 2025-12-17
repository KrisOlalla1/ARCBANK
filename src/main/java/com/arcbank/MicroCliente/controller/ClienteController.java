package com.arcbank.MicroCliente.controller;

import com.arcbank.MicroCliente.dto.ClienteRequestDTO;
import com.arcbank.MicroCliente.dto.ClienteResponseDTO;
import com.arcbank.MicroCliente.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gestión de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Crear cliente")
    @PostMapping
    public ClienteResponseDTO crear(@Valid @RequestBody ClienteRequestDTO request) {
        return clienteService.crearCliente(request);
    }

    @Operation(summary = "Buscar cliente por ID")
    @GetMapping("/{id}")
    public ClienteResponseDTO buscarPorId(@PathVariable Integer id) {
        return clienteService.buscarPorId(id);
    }

    @Operation(summary = "Buscar cliente por identificación")
    @GetMapping("/identificacion/{identificacion}")
    public ClienteResponseDTO buscarPorIdentificacion(@PathVariable String identificacion) {
        return clienteService.buscarPorIdentificacion(identificacion);
    }

    @Operation(summary = "Actualizar estado del cliente")
    @PutMapping("/{id}/estado/{estado}")
    public ClienteResponseDTO actualizarEstado(
            @PathVariable Integer id,
            @PathVariable String estado) {
        return clienteService.actualizarEstado(id, estado);
    }
}
