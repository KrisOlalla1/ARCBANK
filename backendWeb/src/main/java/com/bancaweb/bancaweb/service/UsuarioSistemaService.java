package com.bancaweb.bancaweb.service;

import com.bancaweb.bancaweb.model.UsuarioSistema;
import com.bancaweb.bancaweb.repository.UsuarioSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioSistemaService {

    @Autowired
    private UsuarioSistemaRepository usuarioSistemaRepository;

    @Autowired
    private CoreClientService coreClientService;

    // Registrar un nuevo usuario (valida primero con el Core)
    public UsuarioSistema registrarUsuario(String nombreUsuario, String claveHash, String tipoIdentificacion, String identificacion, Integer idSucursal) {

        // 1. Validar si el cliente existe en el Core
        boolean existeCliente = coreClientService.clienteExiste(tipoIdentificacion, identificacion);
        if (!existeCliente) {
            throw new RuntimeException("El cliente con identificación " + identificacion + " no existe en el Core o no está activo.");
        }

        // 2. Crear el usuario del sistema (solo si el cliente existe en el Core)
        UsuarioSistema usuario = new UsuarioSistema();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setClaveHash(claveHash);
        usuario.setIdSucursal(idSucursal);
        usuario.setIdentificacion(identificacion);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setEstado("ACTIVO");

        return usuarioSistemaRepository.save(usuario);
    }

    // Buscar usuario por nombre
    public Optional<UsuarioSistema> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioSistemaRepository.findByNombreUsuario(nombreUsuario);
    }

    // Registrar último acceso
    public void registrarUltimoAcceso(UsuarioSistema usuario) {
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioSistemaRepository.save(usuario);
    }
}
