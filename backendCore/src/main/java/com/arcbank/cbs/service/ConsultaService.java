package com.arcbank.cbs.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arcbank.cbs.dto.ClienteValidacionResponse;
import com.arcbank.cbs.dto.CuentaDetalleResponse;
import com.arcbank.cbs.dto.MovimientosResponse;
import com.arcbank.cbs.dto.PosicionConsolidadaResponse;
import com.arcbank.cbs.exception.CuentaNoEncontradaException;
import com.arcbank.cbs.model.account.TipoCuentaAhorro;
import com.arcbank.cbs.model.admin.Sucursal;
import com.arcbank.cbs.model.client.Cliente;
import com.arcbank.cbs.model.client.Empresa;
import com.arcbank.cbs.model.client.Persona;
import com.arcbank.cbs.model.passive.CuentaAhorro;
import com.arcbank.cbs.model.passive.Transaccion;
import com.arcbank.cbs.repository.ClienteRepository;
import com.arcbank.cbs.repository.CuentaAhorroRepository;
import com.arcbank.cbs.repository.TransaccionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final CuentaAhorroRepository cuentaAhorroRepository;
    private final TransaccionRepository transaccionRepository;
    private final ClienteRepository clienteRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional(readOnly = true)
    public ClienteValidacionResponse validarCliente(String identificacion) {
        log.info("Validando existencia de cliente: {}", identificacion);
        
        Optional<Cliente> clienteOpt = clienteRepository.findByIdentificacion(identificacion);
        
        if (clienteOpt.isEmpty()) {
            return ClienteValidacionResponse.noEncontrado(identificacion);
        }
        
        Cliente cliente = clienteOpt.get();
        String nombreCompleto;
        
        if ("P".equals(cliente.getTipoCliente())) {
            Persona persona = entityManager.find(Persona.class, cliente.getIdCliente());
            nombreCompleto = persona != null ? persona.getNombres() + " " + persona.getApellidos() : "N/A";
        } else {
            Empresa empresa = entityManager.find(Empresa.class, cliente.getIdCliente());
            nombreCompleto = empresa != null ? empresa.getRazonSocial() : "N/A";
        }
        
        return ClienteValidacionResponse.encontrado(
            cliente.getIdCliente(),
            cliente.getIdentificacion(),
            cliente.getTipoCliente(),
            nombreCompleto,
            cliente.getEstado()
        );
    }

    @Transactional(readOnly = true)
    public PosicionConsolidadaResponse obtenerPosicionConsolidada(String identificacion) {
        log.info("Consultando posición consolidada para cliente: {}", identificacion);

        Cliente cliente = clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + identificacion));

        List<CuentaAhorro> cuentas = cuentaAhorroRepository.findCuentasActivasByCliente(cliente.getIdCliente());

        BigDecimal saldoTotalActivo = BigDecimal.ZERO;
        BigDecimal saldoTotalDisponible = BigDecimal.ZERO;
        List<PosicionConsolidadaResponse.CuentaResumen> cuentasResumen = new ArrayList<>();

        for (CuentaAhorro cuenta : cuentas) {
            saldoTotalActivo = saldoTotalActivo.add(cuenta.getSaldoActual());
            saldoTotalDisponible = saldoTotalDisponible.add(cuenta.getSaldoDisponible());

            TipoCuentaAhorro tipoCuenta = entityManager.find(TipoCuentaAhorro.class, cuenta.getIdTipoCuenta());
            Sucursal sucursal = entityManager.find(Sucursal.class, cuenta.getIdSucursalApertura());

            PosicionConsolidadaResponse.CuentaResumen resumen = new PosicionConsolidadaResponse.CuentaResumen();
            resumen.setIdCuenta(cuenta.getIdCuenta());
            resumen.setNumeroCuenta(cuenta.getNumeroCuenta());
            resumen.setTipoCuenta(tipoCuenta != null ? tipoCuenta.getNombre() : "N/A");
            resumen.setSaldoActual(cuenta.getSaldoActual());
            resumen.setSaldoDisponible(cuenta.getSaldoDisponible());
            resumen.setEstado(cuenta.getEstado());
            resumen.setFechaApertura(cuenta.getFechaApertura());
            resumen.setSucursal(sucursal != null ? sucursal.getNombre() : "N/A");

            cuentasResumen.add(resumen);
        }

        String nombreCliente;
        if ("P".equals(cliente.getTipoCliente())) {
            Persona persona = entityManager.find(Persona.class, cliente.getIdCliente());
            nombreCliente = persona != null ? persona.getNombres() + " " + persona.getApellidos() : "N/A";
        } else {
            Empresa empresa = entityManager.find(Empresa.class, cliente.getIdCliente());
            nombreCliente = empresa != null ? empresa.getRazonSocial() : "N/A";
        }

        PosicionConsolidadaResponse response = new PosicionConsolidadaResponse();
        response.setIdCliente(cliente.getIdCliente());
        response.setNombreCliente(nombreCliente);
        response.setIdentificacion(cliente.getIdentificacion());
        response.setTipoCliente(cliente.getTipoCliente());
        response.setSaldoTotalActivo(saldoTotalActivo);
        response.setSaldoTotalDisponible(saldoTotalDisponible);
        response.setTotalCuentas(cuentas.size());
        response.setFechaConsulta(LocalDate.now());
        response.setCuentas(cuentasResumen);

        log.info("Posición consolidada obtenida. Total cuentas: {}, Saldo total: {}", 
                 cuentas.size(), saldoTotalActivo);

        return response;
    }

    @Transactional(readOnly = true)
    public MovimientosResponse obtenerMovimientos(String numeroCuenta, LocalDate fechaInicio, LocalDate fechaFin) {
        log.info("Consultando movimientos de cuenta: {} desde {} hasta {}", 
                 numeroCuenta, fechaInicio, fechaFin);

        CuentaAhorro cuenta = cuentaAhorroRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNoEncontradaException(numeroCuenta));

        List<Transaccion> transacciones;
        if (fechaInicio != null && fechaFin != null) {
            LocalDateTime inicio = fechaInicio.atStartOfDay();
            LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
            transacciones = transaccionRepository.findByIdCuentaAndFechaBetween(
                    cuenta.getIdCuenta(), inicio, fin);
        } else {
            transacciones = transaccionRepository.findTransaccionesCompletadasByCuenta(cuenta.getIdCuenta());
        }

        List<MovimientosResponse.MovimientoDetalle> movimientos = new ArrayList<>();
        for (Transaccion tx : transacciones) {
            MovimientosResponse.MovimientoDetalle detalle = new MovimientosResponse.MovimientoDetalle();
            detalle.setIdTransaccion(tx.getIdTransaccion());
            detalle.setFechaHora(tx.getFechaHora());
            detalle.setTipo(tx.getTipo());
            detalle.setTipoMovimiento(determinarTipoMovimiento(tx.getTipo()));
            detalle.setMonto(tx.getMonto());
            detalle.setSaldoPost(tx.getBalance());
            detalle.setDescripcion(tx.getDescripcion());
            detalle.setReferencia(tx.getReferencia());
            detalle.setEstado(tx.getEstado());

            movimientos.add(detalle);
        }

        MovimientosResponse response = new MovimientosResponse();
        response.setNumeroCuenta(numeroCuenta);
        response.setTotalMovimientos(movimientos.size());
        response.setMovimientos(movimientos);

        log.info("Movimientos obtenidos. Total: {}", movimientos.size());

        return response;
    }


    private String determinarTipoMovimiento(String tipoTransaccion) {
        return switch (tipoTransaccion) {
            case "RETIRO" -> "DEBITO";
            case "DEPOSITO" -> "CREDITO";
            default -> "INDEFINIDO";
        };
    }


    @Transactional(readOnly = true)
    public CuentaDetalleResponse obtenerDetalleCuenta(String numeroCuenta) {
        log.info("Consultando detalles de cuenta: {}", numeroCuenta);

        Optional<CuentaAhorro> cuentaOpt = cuentaAhorroRepository.findByNumeroCuenta(numeroCuenta);
        
        if (cuentaOpt.isEmpty()) {
            return CuentaDetalleResponse.builder()
                    .numeroCuenta(numeroCuenta)
                    .existe(false)
                    .build();
        }

        CuentaAhorro cuenta = cuentaOpt.get();
        Cliente cliente = clienteRepository.findById(cuenta.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado para cuenta: " + numeroCuenta));

        TipoCuentaAhorro tipoCuenta = entityManager.find(TipoCuentaAhorro.class, cuenta.getIdTipoCuenta());

        CuentaDetalleResponse.CuentaDetalleResponseBuilder builder = CuentaDetalleResponse.builder()
                .numeroCuenta(cuenta.getNumeroCuenta())
                .tipoCuenta(tipoCuenta != null ? tipoCuenta.getNombre() : "AHORRO")
                .saldoActual(cuenta.getSaldoActual())
                .saldoDisponible(cuenta.getSaldoDisponible())
                .estado(cuenta.getEstado())
                .tipoCliente(cliente.getTipoCliente())
                .identificacion(cliente.getIdentificacion())
                .existe(true);

        if ("P".equals(cliente.getTipoCliente())) {
            Persona persona = entityManager.find(Persona.class, cliente.getIdCliente());
            if (persona != null) {
                builder.nombres(persona.getNombres())
                       .apellidos(persona.getApellidos());
            }
        } else {
            Empresa empresa = entityManager.find(Empresa.class, cliente.getIdCliente());
            if (empresa != null) {
                builder.razonSocial(empresa.getRazonSocial());
            }
        }

        CuentaDetalleResponse response = builder.build();
        log.info("Detalles de cuenta obtenidos: {} - Cliente: {} {}", 
                numeroCuenta, response.getNombres(), response.getApellidos());

        return response;
    }
}