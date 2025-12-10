package com.arcbank.cuentas.controller;

import com.arcbank.cuentas.dto.TipoCuentaAhorroDTO;
import com.arcbank.cuentas.service.TipoCuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas/tipos")
public class TipoCuentaController {

    private final TipoCuentaService service;

    public TipoCuentaController(TipoCuentaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TipoCuentaAhorroDTO> create(@RequestBody TipoCuentaAhorroDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCuentaAhorroDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TipoCuentaAhorroDTO>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCuentaAhorroDTO> update(@PathVariable Integer id, @RequestBody TipoCuentaAhorroDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}