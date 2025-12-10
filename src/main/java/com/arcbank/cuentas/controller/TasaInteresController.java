package com.arcbank.cuentas.controller;

import com.arcbank.cuentas.dto.TasaInteresHistorialDTO;
import com.arcbank.cuentas.service.TasaInteresService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas/tasas")
public class TasaInteresController {

    private final TasaInteresService service;

    public TasaInteresController(TasaInteresService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TasaInteresHistorialDTO> create(@RequestBody TasaInteresHistorialDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TasaInteresHistorialDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TasaInteresHistorialDTO>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
