package com.arcbank.cajero.controller;

import com.arcbank.cajero.admin.model.Cajero;
import com.arcbank.cajero.dto.CajeroDTO;
import com.arcbank.cajero.dto.LoginRequest;
import com.arcbank.cajero.dto.LoginResponse;
import com.arcbank.cajero.repository.CajeroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CajeroRepository cajeroRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(CajeroRepository cajeroRepository) {
        this.cajeroRepository = cajeroRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var cajeroOpt = cajeroRepository.findByUsuario(request.getUsuario());
        
        if (cajeroOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Usuario no encontrado");
        }

        Cajero cajero = cajeroOpt.get();
        
        // Validar clave con BCrypt
        if (!passwordEncoder.matches(request.getClave(), cajero.getClave())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciales inválidas");
        }

        // Validar estado activo
        if (!"ACTIVO".equalsIgnoreCase(cajero.getEstado())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Usuario inactivo");
        }

        CajeroDTO dto = new CajeroDTO(
            cajero.getIdCajero(),
            cajero.getUsuario(),
            cajero.getNombreCompleto(),
            cajero.getSucursal() != null ? cajero.getSucursal().getNombre() : "N/A",
            cajero.getEstado()
        );

        return ResponseEntity.ok(new LoginResponse(dto));
    }
}
