package com.arcbank.cuentas.controller;

import com.arcbank.cuentas.dto.TipoCuentaAhorroDTO;
import com.arcbank.cuentas.service.TipoCuentaAhorroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-cuenta")
@RequiredArgsConstructor
public class TipoCuentaAhorroController {

    private final TipoCuentaAhorroService service;

    @PostMapping
    public ResponseEntity<TipoCuentaAhorroDTO> crear(@RequestBody TipoCuentaAhorroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public List<TipoCuentaAhorroDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public TipoCuentaAhorroDTO obtenerPorId(@PathVariable Integer id) {
        return service.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public TipoCuentaAhorroDTO actualizar(@PathVariable Integer id, @RequestBody TipoCuentaAhorroDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
