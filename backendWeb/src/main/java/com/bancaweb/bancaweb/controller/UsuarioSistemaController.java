package com.bancaweb.bancaweb.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bancaweb.bancaweb.dto.LoginRequest;
import com.bancaweb.bancaweb.dto.RegistroUsuarioRequest;
import com.bancaweb.bancaweb.dto.UsuarioResponse;
import com.bancaweb.bancaweb.model.UsuarioSistema;
import com.bancaweb.bancaweb.service.UsuarioSistemaService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioSistemaController {

    @Autowired
    private UsuarioSistemaService usuarioSistemaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

        @PostMapping("/registro")
        public ResponseEntity<?> registrarUsuario(@RequestBody RegistroUsuarioRequest req) {

        try {
            String claveHash = passwordEncoder.encode(req.getClave());

            UsuarioSistema nuevoUsuario = usuarioSistemaService.registrarUsuario(
                req.getNombreUsuario(),
                    claveHash,
                req.getTipoIdentificacion(),
                req.getIdentificacion(),
                req.getIdSucursal()
            );

            return ResponseEntity.ok(new UsuarioResponse(nuevoUsuario, req.getIdentificacion()));

        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(409).body("Nombre de usuario ya existe");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Integer id) {
        Optional<UsuarioSistema> usuario = usuarioSistemaService.buscarPorNombreUsuario(String.valueOf(id));
        return usuario.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpServletResponse response) {

        Optional<UsuarioSistema> usuarioOpt = usuarioSistemaService.buscarPorNombreUsuario(req.getNombreUsuario());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        UsuarioSistema usuario = usuarioOpt.get();

        boolean valido = passwordEncoder.matches(req.getClave(), usuario.getClaveHash());
        if (valido) {
            usuarioSistemaService.registrarUltimoAcceso(usuario);
            try {
                Cookie userCookie = new Cookie("NAMCA_USER", usuario.getNombreUsuario());
                userCookie.setPath("/");
                userCookie.setHttpOnly(false);
                userCookie.setMaxAge(7 * 24 * 3600);
                response.addCookie(userCookie);

                Cookie idCookie = new Cookie("NAMCA_ID", String.valueOf(usuario.getIdUsuario()));
                idCookie.setPath("/");
                idCookie.setHttpOnly(false);
                idCookie.setMaxAge(7 * 24 * 3600);
                response.addCookie(idCookie);
            } catch (Exception ex) {
            }

            // Devolver datos del usuario directamente en la respuesta
            return ResponseEntity.ok(new UsuarioResponse(usuario, usuario.getIdentificacion()));
        } else {
            return ResponseEntity.badRequest().body("Contraseña incorrecta");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            String principalName = request.getUserPrincipal().getName();
            Optional<UsuarioSistema> usuarioOpt = usuarioSistemaService.buscarPorNombreUsuario(principalName);
            if (usuarioOpt.isPresent()) {
                UsuarioSistema u = usuarioOpt.get();
                return ResponseEntity.ok(new UsuarioResponse(u, u.getIdentificacion()));
            }
        }

        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("NAMCA_USER".equals(c.getName())) {
                    String nombre = c.getValue();
                    Optional<UsuarioSistema> usuarioOpt = usuarioSistemaService.buscarPorNombreUsuario(nombre);
                    if (usuarioOpt.isPresent()) {
                        UsuarioSistema u = usuarioOpt.get();
                        return ResponseEntity.ok(new UsuarioResponse(u, u.getIdentificacion()));
                    }
                }
            }
        }

        return ResponseEntity.status(401).body("Usuario no autenticado");
    }
}
