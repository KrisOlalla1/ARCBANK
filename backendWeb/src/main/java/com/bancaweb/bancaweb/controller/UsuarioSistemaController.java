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

    // 🔹 Registrar un nuevo usuario
        @PostMapping("/registro")
        public ResponseEntity<?> registrarUsuario(@RequestBody RegistroUsuarioRequest req) {

        try {
            // Encriptar la contraseña
            String claveHash = passwordEncoder.encode(req.getClave());

            UsuarioSistema nuevoUsuario = usuarioSistemaService.registrarUsuario(
                req.getNombreUsuario(),
                    claveHash,
                req.getTipoIdentificacion(),
                req.getIdentificacion(),
                req.getIdSucursal()
            );

            // Devolver con identificación para que el frontend la guarde
            return ResponseEntity.ok(new UsuarioResponse(nuevoUsuario, req.getIdentificacion()));

        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(409).body("Nombre de usuario ya existe");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 🔹 Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Integer id) {
        Optional<UsuarioSistema> usuario = usuarioSistemaService.buscarPorNombreUsuario(String.valueOf(id));
        return usuario.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Validar login
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
            // Establecer una cookie simple con el nombre de usuario e idUsuario para que el endpoint /me pueda identificar la sesión
            // No cambiamos el body de la respuesta para mantener compatibilidad con clientes existentes.
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
                // si no se puede setear la cookie, no bloqueamos el login
            }

            return ResponseEntity.ok("Inicio de sesión exitoso");
        } else {
            return ResponseEntity.badRequest().body("Contraseña incorrecta");
        }
    }

    // Nuevo endpoint que devuelve el perfil del usuario autenticado (si se puede identificar)
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        // 1) Intentar usar el Principal de la petición (si existe)
        if (request.getUserPrincipal() != null) {
            String principalName = request.getUserPrincipal().getName();
            Optional<UsuarioSistema> usuarioOpt = usuarioSistemaService.buscarPorNombreUsuario(principalName);
            if (usuarioOpt.isPresent()) {
                UsuarioSistema u = usuarioOpt.get();
                return ResponseEntity.ok(new UsuarioResponse(u, u.getIdentificacion()));
            }
        }

        // 2) Fallback: leer cookie NAMCA_USER
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
