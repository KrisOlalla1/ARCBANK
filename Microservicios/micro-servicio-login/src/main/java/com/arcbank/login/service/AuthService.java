package com.arcbank.login.service;

import com.arcbank.login.model.Cajero;
import com.arcbank.login.model.LoginCajero;
import com.arcbank.login.model.UsuarioSistema;
import com.arcbank.login.repository.CajeroRepository;
import com.arcbank.login.repository.LoginCajeroRepository;
import com.arcbank.login.repository.UsuarioSistemaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final CajeroRepository cajeroRepository;
    private final UsuarioSistemaRepository usuarioSistemaRepository;
    private final LoginCajeroRepository loginCajeroRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(CajeroRepository cajeroRepository, UsuarioSistemaRepository usuarioSistemaRepository, LoginCajeroRepository loginCajeroRepository, PasswordEncoder passwordEncoder) {
        this.cajeroRepository = cajeroRepository;
        this.usuarioSistemaRepository = usuarioSistemaRepository;
        this.loginCajeroRepository = loginCajeroRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public Optional<Cajero> autenticarCajero(String usuario, String clave) {
        Optional<Cajero> cajeroOpt = cajeroRepository.findByUsuario(usuario);

        if (cajeroOpt.isEmpty()) {
            return Optional.empty(); 
        }

        Cajero cajero = cajeroOpt.get();

        if (!passwordEncoder.matches(clave, cajero.getClave())) {
            return Optional.empty(); 
        }

        if (!"ACTIVO".equalsIgnoreCase(cajero.getEstado())) {
            return Optional.empty();
        }

        registrarLoginCajero(cajero);

        return Optional.of(cajero);
    }

    public Optional<UsuarioSistema> autenticarUsuarioSistema(String nombreUsuario, String clave) {
        Optional<UsuarioSistema> usuarioOpt = usuarioSistemaRepository.findByNombreUsuario(nombreUsuario);

        if (usuarioOpt.isEmpty()) {
            return Optional.empty(); 
        }

        UsuarioSistema usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(clave, usuario.getClaveHash())) {
            return Optional.empty();
        }

        // 2. Validar estado
        if ("INACTIVO".equalsIgnoreCase(usuario.getEstado()) || "BLOQUEADO".equalsIgnoreCase(usuario.getEstado())) {
             return Optional.empty(); 
        }

        usuario.setUltimoAcceso(java.time.LocalDateTime.now());
        usuarioSistemaRepository.save(usuario);

        return Optional.of(usuario);
    }

    private void registrarLoginCajero(Cajero cajero) {
        LoginCajero login = new LoginCajero();
        login.setCajero(cajero);
        loginCajeroRepository.save(login);
    }
}