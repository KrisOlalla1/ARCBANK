package com.arcbank.cuentas.service;

import com.arcbank.cuentas.dto.request.TipoCuentaAhorroRequest;
import com.arcbank.cuentas.dto.response.TipoCuentaAhorroDTO;
import com.arcbank.cuentas.exception.ResourceNotFoundException;
import com.arcbank.cuentas.model.TipoCuentaAhorro;
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
public class TipoCuentaService {

    private final TipoCuentaRepository tipoRepo;

    @Transactional
    public TipoCuentaAhorroDTO create(TipoCuentaAhorroRequest request) {
        TipoCuentaAhorro t = new TipoCuentaAhorro();
        t.setNombre(request.getNombre());
        t.setDescripcion(request.getDescripcion());
        t.setTasaInteresMaxima(request.getTasaInteresMaxima());
        t.setAmortizacion(request.getAmortizacion());
        t.setActivo(request.getActivo());

        log.info("Tipo de cuenta creada: {}", t.getNombre());
        return toDTO(tipoRepo.save(t));
    }

    @Transactional
    public TipoCuentaAhorroDTO update(Integer id, TipoCuentaAhorroRequest request) {
        TipoCuentaAhorro t = tipoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cuenta no encontrado: " + id));
        t.setNombre(request.getNombre());
        t.setDescripcion(request.getDescripcion());
        t.setTasaInteresMaxima(request.getTasaInteresMaxima());
        t.setAmortizacion(request.getAmortizacion());
        t.setActivo(request.getActivo());
        log.info("Tipo de cuenta actualizada: {}", id);
        return toDTO(tipoRepo.save(t));
    }

    public TipoCuentaAhorroDTO findById(Integer id) {
        return tipoRepo.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cuenta no encontrado: " + id));
    }

    public List<TipoCuentaAhorroDTO> findAll() {
        return tipoRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Integer id) {
        TipoCuentaAhorro t = tipoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cuenta no encontrado: " + id));
        tipoRepo.delete(t);
        log.info("Tipo de cuenta eliminada: {}", id);
    }

    private TipoCuentaAhorroDTO toDTO(TipoCuentaAhorro t) {
        TipoCuentaAhorroDTO dto = new TipoCuentaAhorroDTO();
        dto.setIdTipoCuenta(t.getIdTipoCuenta());
        dto.setNombre(t.getNombre());
        dto.setDescripcion(t.getDescripcion());
        dto.setTasaInteresMaxima(t.getTasaInteresMaxima());
        dto.setAmortizacion(t.getAmortizacion());
        dto.setActivo(t.getActivo());
        return dto;
    }
}
