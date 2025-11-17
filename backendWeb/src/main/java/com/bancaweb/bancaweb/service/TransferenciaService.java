package com.bancaweb.bancaweb.service;

import com.bancaweb.bancaweb.dto.TransferenciaRequest;
import com.bancaweb.bancaweb.model.OperacionCaja;
import com.bancaweb.bancaweb.model.UsuarioSistema;
import com.bancaweb.bancaweb.repository.OperacionCajaRepository;
import com.bancaweb.bancaweb.repository.UsuarioSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
 

@Service
public class TransferenciaService {

    @Autowired
    private CoreClientService coreClientService;

    @Autowired
    private OperacionCajaRepository operacionCajaRepository;

    @Autowired
    private UsuarioSistemaRepository usuarioSistemaRepository;

    /**
     * Ejecuta una transferencia en el Core y audita el resultado en la Banca Web.
     */
    public String realizarTransferencia(TransferenciaRequest request) {

        if (request.getMonto().compareTo(new java.math.BigDecimal("0.01")) < 0) {
            throw new IllegalArgumentException("El monto de la transferencia debe ser positivo.");
        }

        Integer idUsuario = java.util.Objects.requireNonNull(request.getIdUsuarioWeb(), "idUsuarioWeb es requerido");
        UsuarioSistema usuario = usuarioSistemaRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario web no encontrado."));

        // 1) Ejecutar la transferencia en el Core vía HTTP
        String resultadoCore = coreClientService.ejecutarTransferencia(
                request.getCuentaOrigen(),
                request.getCuentaDestino(),
                request.getMonto(),
                request.getDescripcion(),
                usuario.getIdSucursal()
        );

        // 2) Auditoría local en OperacionCaja (sin idCuenta Core disponible por API; lo dejamos nulo)
        String estadoOperacionWeb = "COMPLETADA".equalsIgnoreCase(resultadoCore) ? "EXITOSA" : "FALLIDA";

        OperacionCaja operacion = new OperacionCaja();
        operacion.setUsuario(usuario);
        // El esquema actual restringe Tipo mediante CHECK; usamos un valor permitido
        // equivalente a la salida de fondos desde la cuenta de origen.
        operacion.setTipo("RETIRO");
        operacion.setIdCuenta(null);
        operacion.setMonto(request.getMonto());
        operacion.setDescripcion((request.getDescripcion() != null ? request.getDescripcion() + " - " : "")
                + "Origen: " + request.getCuentaOrigen() + " -> Destino: " + request.getCuentaDestino());
        operacion.setFechaOperacion(LocalDateTime.now());
        operacion.setEstadoOperacion(estadoOperacionWeb);

        operacionCajaRepository.save(operacion);

        if (estadoOperacionWeb.equals("FALLIDA")) {
            throw new RuntimeException("Transferencia Fallida en Core");
        }

        return "Transferencia exitosa. ID de auditoría web: " + operacion.getIdOperacion();
    }
}