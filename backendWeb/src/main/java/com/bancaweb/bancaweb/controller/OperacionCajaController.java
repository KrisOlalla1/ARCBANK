package com.bancaweb.bancaweb.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bancaweb.bancaweb.model.OperacionCaja;
import com.bancaweb.bancaweb.model.UsuarioSistema;
import com.bancaweb.bancaweb.repository.UsuarioSistemaRepository;
import com.bancaweb.bancaweb.service.OperacionCajaService;

@RestController
@RequestMapping("/api/operaciones")
public class OperacionCajaController {

    @Autowired
    private OperacionCajaService operacionCajaService;

    @Autowired
    private UsuarioSistemaRepository usuarioSistemaRepository;

    @PostMapping
    public ResponseEntity<?> registrarOperacion(@RequestBody OperacionCaja operacion) {
        return ResponseEntity.ok("Operación registrada correctamente (simulado)");
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> consultarMovimientos(
            @PathVariable Integer idUsuario,
            @RequestParam String desde,
            @RequestParam String hasta) {

        if (idUsuario == null) {
            return ResponseEntity.badRequest().body("ID de usuario inválido");
        }

        Optional<UsuarioSistema> usuarioOpt = usuarioSistemaRepository.findById(idUsuario);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        LocalDate fechaDesde = LocalDate.parse(desde);
        LocalDate fechaHasta = LocalDate.parse(hasta);

        List<OperacionCaja> operaciones = operacionCajaService.obtenerMovimientosPorFecha(
                usuarioOpt.get(), fechaDesde, fechaHasta
        );

        return ResponseEntity.ok(operaciones);
    }
}
