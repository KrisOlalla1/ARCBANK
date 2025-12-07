package com.arcbank.login.controller;

import com.arcbank.login.dto.LoginCajeroDTO;
import com.arcbank.login.dto.LoginRequest;
import com.arcbank.login.dto.LoginResponse;
import com.arcbank.login.dto.LoginUsuarioSistemaDTO;
import com.arcbank.login.model.Cajero;
import com.arcbank.login.model.UsuarioSistema;
import com.arcbank.login.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        
        LoginResponse response = new LoginResponse();
        String tipoLogin = request.getTipoLogin() != null ? request.getTipoLogin().toUpperCase(Locale.ROOT) : "CAJERO"; // Default a CAJERO

        if ("CAJERO".equals(tipoLogin)) {
            return handleCajeroLogin(request, response);
        } else if ("WEB".equals(tipoLogin) || "SISTEMA".equals(tipoLogin)) {
            return handleUsuarioSistemaLogin(request, response);
        } else {
            response.setAutenticado(false);
            response.setMensaje("Tipo de login no soportado.");
            response.setTipoUsuario("N/A");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    private ResponseEntity<LoginResponse> handleCajeroLogin(LoginRequest request, LoginResponse response) {
        var cajeroOpt = authService.autenticarCajero(request.getUsuario(), request.getClave());

        if (cajeroOpt.isEmpty()) {
            response.setAutenticado(false);
            response.setMensaje("Usuario o credenciales de Cajero inválidas / Usuario inactivo.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Cajero cajero = cajeroOpt.get();

        LoginCajeroDTO dto = new LoginCajeroDTO(
            cajero.getIdCajero(),
            cajero.getUsuario(),
            cajero.getNombreCompleto(),
            cajero.getIdSucursal(),
            cajero.getEstado()
        );

        response.setAutenticado(true);
        response.setUserInfo(dto);
        response.setMensaje("Login de Cajero exitoso.");
        response.setTipoUsuario("CAJERO");

        return ResponseEntity.ok(response);
    }

    private ResponseEntity<LoginResponse> handleUsuarioSistemaLogin(LoginRequest request, LoginResponse response) {
        var usuarioOpt = authService.autenticarUsuarioSistema(request.getUsuario(), request.getClave());

        if (usuarioOpt.isEmpty()) {
            response.setAutenticado(false);
            response.setMensaje("Usuario o credenciales de Sistema inválidas / Usuario inactivo o bloqueado.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        UsuarioSistema usuario = usuarioOpt.get();

        LoginUsuarioSistemaDTO dto = new LoginUsuarioSistemaDTO(
            usuario.getIdUsuario(),
            usuario.getNombreUsuario(),
            usuario.getIdSucursal(),
            usuario.getEstado()
        );

        response.setAutenticado(true);
        response.setUserInfo(dto);
        response.setMensaje("Login de Usuario Sistema exitoso.");
        response.setTipoUsuario("SISTEMA");

        return ResponseEntity.ok(response);
    }
}