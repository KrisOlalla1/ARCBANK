package com.arcbank.cbs.controller;

import com.arcbank.cbs.dto.DepositoRequest;
import com.arcbank.cbs.dto.RetiroRequest;
import com.arcbank.cbs.dto.TransaccionResponse;
import com.arcbank.cbs.model.client.Cliente;
import com.arcbank.cbs.model.client.Persona;
import com.arcbank.cbs.model.client.Empresa;
import com.arcbank.cbs.model.passive.CuentaAhorro;
import com.arcbank.cbs.model.account.TipoCuentaAhorro;
import com.arcbank.cbs.repository.ClienteRepository;
import com.arcbank.cbs.repository.CuentaAhorroRepository;
import com.arcbank.cbs.service.TransaccionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para endpoints simplificados usados por el frontend del Cajero
 * Estos endpoints son más simples que los de ConsultaController
 * Los endpoints NO llevan prefijo /api/core porque el Gateway ya hace StripPrefix
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CajeroIntegrationController {

    private final ClienteRepository clienteRepository;
    private final CuentaAhorroRepository cuentaAhorroRepository;
    private final TransaccionService transaccionService;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * GET /clientes/cedula/{cedula}
     * Retorna información básica del cliente por cédula/identificación
     */
    @GetMapping("/clientes/cedula/{cedula}")
    public ResponseEntity<?> getClienteByCedula(@PathVariable String cedula) {
        log.info("Buscando cliente por cédula: {}", cedula);
        
        return clienteRepository.findByIdentificacion(cedula)
            .map(cliente -> {
                Map<String, Object> response = new HashMap<>();
                response.put("idCliente", cliente.getIdCliente());
                response.put("identificacion", cliente.getIdentificacion());
                response.put("tipoCliente", cliente.getTipoCliente());
                
                String nombres = "";
                String apellidos = "";
                
                if ("P".equals(cliente.getTipoCliente())) {
                    Persona persona = entityManager.find(Persona.class, cliente.getIdCliente());
                    if (persona != null) {
                        nombres = persona.getNombres();
                        apellidos = persona.getApellidos();
                    }
                } else {
                    Empresa empresa = entityManager.find(Empresa.class, cliente.getIdCliente());
                    if (empresa != null) {
                        nombres = empresa.getRazonSocial();
                        apellidos = "";
                    }
                }
                
                response.put("nombres", nombres);
                response.put("apellidos", apellidos);
                response.put("estado", cliente.getEstado());
                
                return ResponseEntity.ok((Object) response);
            })
            .orElse(ResponseEntity.status(404).body("Cliente no encontrado"));
    }

    /**
     * GET /cuentas/numero/{numero}
     * Retorna información de la cuenta por número de cuenta
     */
    @GetMapping("/cuentas/numero/{numero}")
    public ResponseEntity<?> getCuentaByNumero(@PathVariable String numero) {
        log.info("Buscando cuenta por número: {}", numero);
        
        return cuentaAhorroRepository.findByNumeroCuenta(numero)
            .map(cuenta -> {
                Map<String, Object> response = new HashMap<>();
                response.put("idCuenta", cuenta.getIdCuenta());
                response.put("numeroCuenta", cuenta.getNumeroCuenta());
                response.put("balance", cuenta.getSaldoActual());
                response.put("saldoDisponible", cuenta.getSaldoDisponible());
                response.put("estado", cuenta.getEstado());
                
                // Obtener tipo de cuenta
                TipoCuentaAhorro tipoCuenta = entityManager.find(TipoCuentaAhorro.class, cuenta.getIdTipoCuenta());
                response.put("tipo", tipoCuenta != null ? tipoCuenta.getNombre() : "N/A");
                
                // Obtener información del cliente
                Cliente cliente = entityManager.find(Cliente.class, cuenta.getIdCliente());
                if (cliente != null) {
                    response.put("cedula", cliente.getIdentificacion());
                    
                    String nombres = "";
                    String apellidos = "";
                    
                    if ("P".equals(cliente.getTipoCliente())) {
                        Persona persona = entityManager.find(Persona.class, cliente.getIdCliente());
                        if (persona != null) {
                            nombres = persona.getNombres();
                            apellidos = persona.getApellidos();
                        }
                    } else {
                        Empresa empresa = entityManager.find(Empresa.class, cliente.getIdCliente());
                        if (empresa != null) {
                            nombres = empresa.getRazonSocial();
                            apellidos = "";
                        }
                    }
                    
                    response.put("nombres", nombres);
                    response.put("apellidos", apellidos);
                    response.put("nombre", (nombres + " " + apellidos).trim());
                }
                
                return ResponseEntity.ok((Object) response);
            })
            .orElse(ResponseEntity.status(404).body("Cuenta no encontrada"));
    }

    /**
     * GET /cuentas/cedula/{cedula}
     * Retorna las cuentas del cliente por su cédula
     */
    @GetMapping("/cuentas/cedula/{cedula}")
    public ResponseEntity<?> getCuentasByCedula(@PathVariable String cedula) {
        log.info("Buscando cuentas por cédula: {}", cedula);
        
        return clienteRepository.findByIdentificacion(cedula)
            .map(cliente -> {
                var cuentas = cuentaAhorroRepository.findCuentasActivasByCliente(cliente.getIdCliente());
                
                if (cuentas.isEmpty()) {
                    return ResponseEntity.status(404).body("No se encontraron cuentas activas");
                }
                
                // Retornar la primera cuenta activa
                CuentaAhorro cuenta = cuentas.get(0);
                
                Map<String, Object> response = new HashMap<>();
                response.put("idCuenta", cuenta.getIdCuenta());
                response.put("numeroCuenta", cuenta.getNumeroCuenta());
                response.put("balance", cuenta.getSaldoActual());
                response.put("saldoDisponible", cuenta.getSaldoDisponible());
                response.put("estado", cuenta.getEstado());
                
                TipoCuentaAhorro tipoCuenta = entityManager.find(TipoCuentaAhorro.class, cuenta.getIdTipoCuenta());
                response.put("tipo", tipoCuenta != null ? tipoCuenta.getNombre() : "N/A");
                
                String nombres = "";
                String apellidos = "";
                
                if ("P".equals(cliente.getTipoCliente())) {
                    Persona persona = entityManager.find(Persona.class, cliente.getIdCliente());
                    if (persona != null) {
                        nombres = persona.getNombres();
                        apellidos = persona.getApellidos();
                    }
                } else {
                    Empresa empresa = entityManager.find(Empresa.class, cliente.getIdCliente());
                    if (empresa != null) {
                        nombres = empresa.getRazonSocial();
                        apellidos = "";
                    }
                }
                
                response.put("nombres", nombres);
                response.put("apellidos", apellidos);
                response.put("nombre", (nombres + " " + apellidos).trim());
                response.put("cedula", cliente.getIdentificacion());
                
                return ResponseEntity.ok((Object) response);
            })
            .orElse(ResponseEntity.status(404).body("Cliente no encontrado"));
    }

    /**
     * POST /transacciones/deposito
     * Procesa un depósito (usado por frontend cajero)
     */
    @PostMapping("/transacciones/deposito")
    public ResponseEntity<TransaccionResponse> procesarDeposito(@Valid @RequestBody DepositoRequest request) {
        log.info("Cajero API: Depósito para cuenta: {}", request.getNumeroCuenta());
        TransaccionResponse response = transaccionService.depositar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * POST /transacciones/retiro
     * Procesa un retiro (usado por frontend cajero)
     */
    @PostMapping("/transacciones/retiro")
    public ResponseEntity<TransaccionResponse> procesarRetiro(@Valid @RequestBody RetiroRequest request) {
        log.info("Cajero API: Retiro para cuenta: {}", request.getNumeroCuenta());
        TransaccionResponse response = transaccionService.retirar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
