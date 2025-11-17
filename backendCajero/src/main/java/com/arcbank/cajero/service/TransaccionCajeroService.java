package com.arcbank.cajero.service;

import com.arcbank.cajero.accounts.model.TransaccionCajero;
import com.arcbank.cajero.admin.model.Cajero;
import com.arcbank.cajero.dto.OperacionCajeroRequest;
import com.arcbank.cajero.dto.TransaccionCoreRequest;
import com.arcbank.cajero.dto.TransaccionCoreResponse;
import com.arcbank.cajero.repository.CajeroRepository;
import com.arcbank.cajero.repository.TransaccionCajeroRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransaccionCajeroService {

    private final TransaccionCajeroRepository repository;
    private final CajeroRepository cajeroRepository;
    private final CoreSyncService coreSyncService;

    public TransaccionCajeroService(TransaccionCajeroRepository repository,
                                    CajeroRepository cajeroRepository,
                                    CoreSyncService coreSyncService) {
        this.repository = repository;
        this.cajeroRepository = cajeroRepository;
        this.coreSyncService = coreSyncService;
    }

    public TransaccionCajero depositar(OperacionCajeroRequest request) {
        Integer id = java.util.Objects.requireNonNull(request.getIdCajero(), "idCajero es requerido");
        Cajero cajero = cajeroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cajero no encontrado: " + request.getIdCajero()));

        TransaccionCajero tx = new TransaccionCajero();
        tx.setNumeroCuenta(request.getNumeroCuenta());
        tx.setMonto(request.getMonto());
        tx.setDescripcion(request.getDescripcion());
        tx.setTipo("DEPOSITO");
        tx.setFechaHora(LocalDateTime.now());
        tx.setReferencia(normalizarReferencia(request.getReferencia(), "CJ-DEP"));
        tx.setEstado("PENDIENTE");
        tx.setCajero(cajero);
        tx = repository.save(tx);

        TransaccionCoreRequest coreReq = new TransaccionCoreRequest();
        coreReq.setNumeroCuenta(tx.getNumeroCuenta());
        coreReq.setTipo(tx.getTipo());
        coreReq.setMonto(tx.getMonto());
        coreReq.setDescripcion(tx.getDescripcion());
        coreReq.setReferencia(tx.getReferencia());
        coreReq.setIdSucursal(cajero.getSucursal().getIdSucursal());

        try {
            TransaccionCoreResponse coreResp = coreSyncService.enviar(coreReq);
            tx.setEstado("SINCRONIZADA");
            tx.setBalance(coreResp != null ? coreResp.getBalance() : tx.getBalance());
            tx.setFechaHora(coreResp != null ? coreResp.getFechaHora() : tx.getFechaHora());
        } catch (Exception ex) {
            tx.setEstado("FALLIDA");
        }

        return repository.save(tx);
    }

    public TransaccionCajero retirar(OperacionCajeroRequest request) {
        Integer id = java.util.Objects.requireNonNull(request.getIdCajero(), "idCajero es requerido");
        Cajero cajero = cajeroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cajero no encontrado: " + request.getIdCajero()));

        TransaccionCajero tx = new TransaccionCajero();
        tx.setNumeroCuenta(request.getNumeroCuenta());
        tx.setMonto(request.getMonto());
        tx.setDescripcion(request.getDescripcion());
        tx.setTipo("RETIRO");
        tx.setFechaHora(LocalDateTime.now());
        tx.setReferencia(normalizarReferencia(request.getReferencia(), "CJ-RET"));
        tx.setEstado("PENDIENTE");
        tx.setCajero(cajero);
        tx = repository.save(tx);

        TransaccionCoreRequest coreReq = new TransaccionCoreRequest();
        coreReq.setNumeroCuenta(tx.getNumeroCuenta());
        coreReq.setTipo(tx.getTipo());
        coreReq.setMonto(tx.getMonto());
        coreReq.setDescripcion(tx.getDescripcion());
        coreReq.setReferencia(tx.getReferencia());
        coreReq.setIdSucursal(cajero.getSucursal().getIdSucursal());

        try {
            TransaccionCoreResponse coreResp = coreSyncService.enviar(coreReq);
            tx.setEstado("SINCRONIZADA");
            tx.setBalance(coreResp != null ? coreResp.getBalance() : tx.getBalance());
            tx.setFechaHora(coreResp != null ? coreResp.getFechaHora() : tx.getFechaHora());
        } catch (Exception ex) {
            tx.setEstado("FALLIDA");
        }

        return repository.save(tx);
    }

    private String normalizarReferencia(String referencia, String prefijo) {
        if (referencia != null && !referencia.isBlank()) return referencia;
        String ts = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
                .format(java.time.LocalDateTime.now());
        String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return prefijo + "-" + ts + "-" + rand;
    }
}
