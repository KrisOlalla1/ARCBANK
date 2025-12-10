package com.arcbank.cuentas.service.impl;

import com.arcbank.cuentas.dto.TipoCuentaAhorroDTO;
import com.arcbank.cuentas.model.TipoCuentaAhorro;
import com.arcbank.cuentas.exception.ResourceNotFoundException;
import com.arcbank.cuentas.repository.TipoCuentaRepository;
import com.arcbank.cuentas.service.TipoCuentaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoCuentaServiceImpl implements TipoCuentaService {

    private final TipoCuentaRepository repository;

    public TipoCuentaServiceImpl(TipoCuentaRepository repository) {
        this.repository = repository;
    }

    @Override
    public TipoCuentaAhorroDTO create(TipoCuentaAhorroDTO dto) {
        TipoCuentaAhorro e = toEntity(dto);
        e = repository.save(e);
        return toDto(e);
    }

    @Override
    public TipoCuentaAhorroDTO update(Integer id, TipoCuentaAhorroDTO dto) {
        TipoCuentaAhorro existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoCuenta not found: " + id));
        existing.setNombre(dto.getNombre());
        existing.setDescripcion(dto.getDescripcion());
        existing.setTasaInteresMaxima(dto.getTasaInteresMaxima());
        existing.setAmortizacion(dto.getAmortizacion());
        existing.setActivo(dto.getActivo());
        existing = repository.save(existing);
        return toDto(existing);
    }

    @Override
    public TipoCuentaAhorroDTO findById(Integer id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("TipoCuenta not found: " + id));
    }

    @Override
    public List<TipoCuentaAhorroDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        TipoCuentaAhorro t = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoCuenta not found: " + id));
        repository.delete(t);
    }

    private TipoCuentaAhorroDTO toDto(TipoCuentaAhorro e) {
        TipoCuentaAhorroDTO d = new TipoCuentaAhorroDTO();
        d.setIdTipoCuenta(e.getIdTipoCuenta());
        d.setNombre(e.getNombre());
        d.setDescripcion(e.getDescripcion());
        d.setTasaInteresMaxima(e.getTasaInteresMaxima());
        d.setAmortizacion(e.getAmortizacion());
        d.setActivo(e.getActivo());
        return d;
    }

    private TipoCuentaAhorro toEntity(TipoCuentaAhorroDTO d) {
        TipoCuentaAhorro e = new TipoCuentaAhorro();
        e.setNombre(d.getNombre());
        e.setDescripcion(d.getDescripcion());
        e.setTasaInteresMaxima(d.getTasaInteresMaxima());
        e.setAmortizacion(d.getAmortizacion());
        e.setActivo(d.getActivo());
        return e;
    }
}