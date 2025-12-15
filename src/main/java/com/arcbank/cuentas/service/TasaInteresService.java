package com.arcbank.cuentas.service;

import com.arcbank.cuentas.dto.request.TasaInteresHistorialRequest;
import com.arcbank.cuentas.dto.response.TasaInteresHistorialDTO;
import com.arcbank.cuentas.exception.ResourceNotFoundException;
import com.arcbank.cuentas.model.TasaInteresHistorial;
import com.arcbank.cuentas.model.TipoCuentaAhorro;
import com.arcbank.cuentas.repository.TasaInteresRepository;
import com.arcbank.cuentas.repository.TipoCuentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TasaInteresService {

    private final TasaInteresRepository repo;
    private final TipoCuentaRepository tipoRepo;

    @Transactional
    public TasaInteresHistorialDTO create(TasaInteresHistorialRequest request) {
        TipoCuentaAhorro tipo = tipoRepo.findById(request.getIdTipoCuenta())
                .orElseThrow(() -> new ResourceNotFoundException("TipoCuenta no encontrada: " + request.getIdTipoCuenta()));

        TasaInteresHistorial t = new TasaInteresHistorial();
        t.setTipoCuenta(tipo);
        t.setTasaInteresAnual(request.getTasaInteresAnual());
        t.setFechaInicio(request.getFechaInicio());
        t.setFechaFin(request.getFechaFin());

        log.info("Tasa de interés creada para tipo: {}", tipo.getNombre());
        return toDTO(repo.save(t));
    }

    public TasaInteresHistorialDTO findById(Integer id) {
        return repo.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Tasa de interés no encontrada: " + id));
    }

    public List<TasaInteresHistorialDTO> findAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Integer id) {
        TasaInteresHistorial t = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tasa de interés no encontrada: " + id));
        repo.delete(t);
        log.info("Tasa de interés eliminada: {}", id);
    }

    private TasaInteresHistorialDTO toDTO(TasaInteresHistorial t) {
        TasaInteresHistorialDTO dto = new TasaInteresHistorialDTO();
        dto.setIdTasaHistorial(t.getIdTasaHistorial());
        dto.setIdTipoCuenta(t.getTipoCuenta().getIdTipoCuenta());
        dto.setTasaInteresAnual(t.getTasaInteresAnual());
        dto.setFechaInicio(t.getFechaInicio());
        dto.setFechaFin(t.getFechaFin());
        return dto;
    }
}
