package com.bancaweb.bancaweb.service;

import com.bancaweb.bancaweb.dto.CoreDtos;
import com.bancaweb.bancaweb.dto.CuentaCoreDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class CoreClientService {

    @Value("${core.api.base}")
    private String coreApiBase;

    private final RestTemplate restTemplate;

    public CoreClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // --- Métodos vía HTTP al Core ---

    public boolean clienteExiste(String tipoIdentificacion, String identificacion) {
        // tipoIdentificacion no es usado por el Core; validamos por identificacion
        String url = coreApiBase + "/api/core/consultas/cliente/" + identificacion + "/validar";
        ResponseEntity<CoreDtos.ClienteValidacionResponse> resp = restTemplate.getForEntity(url, CoreDtos.ClienteValidacionResponse.class);
        CoreDtos.ClienteValidacionResponse body = resp.getBody();
        return body != null && body.isExiste() && "ACTIVO".equalsIgnoreCase(body.getEstado());
    }

    public Integer obtenerIdCliente(String identificacion) {
        String url = coreApiBase + "/api/core/consultas/cliente/" + identificacion + "/validar";
        ResponseEntity<CoreDtos.ClienteValidacionResponse> resp = restTemplate.getForEntity(url, CoreDtos.ClienteValidacionResponse.class);
        CoreDtos.ClienteValidacionResponse body = resp.getBody();
        return body != null && body.isExiste() ? body.getIdCliente() : null;
    }

    public List<CuentaCoreDTO> obtenerPosicionConsolidadaPorIdentificacion(String identificacion) {
        String url = coreApiBase + "/api/core/consultas/posicion-consolidada/" + identificacion;
        ResponseEntity<CoreDtos.PosicionConsolidadaResponse> resp = restTemplate.getForEntity(url, CoreDtos.PosicionConsolidadaResponse.class);
        CoreDtos.PosicionConsolidadaResponse pc = resp.getBody();
        if (pc == null || pc.getCuentas() == null) return List.of();
        return Arrays.asList(pc.getCuentas().stream()
                .map(c -> new CuentaCoreDTO(
                        c.getIdCuenta(),
                        c.getNumeroCuenta(),
                        c.getSaldoActual(),
                        c.getEstado(),
                        c.getTipoCuenta(),
                        pc.getNombreCliente(),
                        pc.getIdentificacion(),
                        c.getSucursal()
                ))
                .toArray(CuentaCoreDTO[]::new));
    }

    public String ejecutarTransferencia(String cuentaOrigen, String cuentaDestino, BigDecimal monto, String descripcion, Integer idSucursal) {
        String url = coreApiBase + "/api/core/transacciones/transferir";
        CoreDtos.CoreTransferRequest req = new CoreDtos.CoreTransferRequest();
        req.setNumeroCuentaOrigen(cuentaOrigen);
        req.setNumeroCuentaDestino(cuentaDestino);
        req.setMonto(monto);
        req.setDescripcion(descripcion);
        req.setIdSucursalTx(idSucursal);

        ResponseEntity<CoreDtos.CoreTransaccionResponse> resp = restTemplate.postForEntity(url, req, CoreDtos.CoreTransaccionResponse.class);
        CoreDtos.CoreTransaccionResponse body = resp.getBody();
        if (body == null) throw new RuntimeException("Respuesta vacía del Core");
        return body.getEstado() != null ? body.getEstado() : "COMPLETADA";
    }

    public CoreDtos.MovimientosResponse obtenerMovimientosPorCuenta(String numeroCuenta, String fechaInicio, String fechaFin) {
        StringBuilder url = new StringBuilder(coreApiBase)
                .append("/api/core/consultas/movimientos/")
                .append(numeroCuenta);
        String sep = "?";
        if (fechaInicio != null && !fechaInicio.isBlank()) {
            url.append(sep).append("fechaInicio=").append(fechaInicio);
            sep = "&";
        }
        if (fechaFin != null && !fechaFin.isBlank()) {
            url.append(sep).append("fechaFin=").append(fechaFin);
        }
        ResponseEntity<CoreDtos.MovimientosResponse> resp = restTemplate.getForEntity(Objects.requireNonNull(url.toString()), CoreDtos.MovimientosResponse.class);
        return Objects.requireNonNullElseGet(resp.getBody(), () -> {
            CoreDtos.MovimientosResponse empty = new CoreDtos.MovimientosResponse();
            empty.setNumeroCuenta(numeroCuenta);
            empty.setTotalMovimientos(0);
            empty.setMovimientos(List.of());
            return empty;
        });
    }
}