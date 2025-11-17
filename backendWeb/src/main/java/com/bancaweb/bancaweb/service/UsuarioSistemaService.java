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

    public UsuarioSistema registrarUsuario(String nombreUsuario, String claveHash, String tipoIdentificacion, String identificacion, Integer idSucursal) {

        boolean existeCliente = coreClientService.clienteExiste(tipoIdentificacion, identificacion);
        if (!existeCliente) {
            throw new RuntimeException("El cliente con identificación " + identificacion + " no existe en el Core o no está activo.");
        }

        UsuarioSistema usuario = new UsuarioSistema();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setClaveHash(claveHash);
        usuario.setIdSucursal(idSucursal);
        usuario.setIdentificacion(identificacion);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setEstado("ACTIVO");

        return usuarioSistemaRepository.save(usuario);
    }

    public Optional<UsuarioSistema> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioSistemaRepository.findByNombreUsuario(nombreUsuario);
    }

    public void registrarUltimoAcceso(UsuarioSistema usuario) {
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioSistemaRepository.save(usuario);
    }
}
