package com.arcbank.cuentas.service.impl;

import com.arcbank.cuentas.dto.TasaInteresHistorialDTO;
import com.arcbank.cuentas.model.TasaInteresHistorial;
import com.arcbank.cuentas.model.TipoCuentaAhorro;
import com.arcbank.cuentas.exception.ResourceNotFoundException;
import com.arcbank.cuentas.repository.TasaInteresRepository;
import com.arcbank.cuentas.repository.TipoCuentaRepository;
import com.arcbank.cuentas.service.TasaInteresService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TasaInteresServiceImpl implements TasaInteresService {

    private final TasaInteresRepository repository;
    private final TipoCuentaRepository tipoRepo;

    public TasaInteresServiceImpl(TasaInteresRepository repository, TipoCuentaRepository tipoRepo) {
        this.repository = repository;
        this.tipoRepo = tipoRepo;
    }

    @Override
    public TasaInteresHistorialDTO create(TasaInteresHistorialDTO dto) {
        TipoCuentaAhorro tipo = tipoRepo.findById(dto.getIdTipoCuenta())
                .orElseThrow(() -> new ResourceNotFoundException("TipoCuenta not found: " + dto.getIdTipoCuenta()));
        TasaInteresHistorial e = new TasaInteresHistorial();
        e.setTipoCuenta(tipo);
        e.setTasaInteresAnual(dto.getTasaInteresAnual());
        e.setFechaInicio(dto.getFechaInicio());
        e.setFechaFin(dto.getFechaFin());
        e = repository.save(e);
        dto.setIdTasaHistorial(e.getIdTasaHistorial());
        return dto;
    }

    @Override
    public TasaInteresHistorialDTO findById(Integer id) {
        return repository.findById(id).map(e -> {
            TasaInteresHistorialDTO d = new TasaInteresHistorialDTO();
            d.setIdTasaHistorial(e.getIdTasaHistorial());
            d.setIdTipoCuenta(e.getTipoCuenta().getIdTipoCuenta());
            d.setTasaInteresAnual(e.getTasaInteresAnual());
            d.setFechaInicio(e.getFechaInicio());
            d.setFechaFin(e.getFechaFin());
            return d;
        }).orElseThrow(() -> new ResourceNotFoundException("Tasa not found: " + id));
    }

    @Override
    public List<TasaInteresHistorialDTO> findAll() {
        return repository.findAll().stream().map(e -> {
            TasaInteresHistorialDTO d = new TasaInteresHistorialDTO();
            d.setIdTasaHistorial(e.getIdTasaHistorial());
            d.setIdTipoCuenta(e.getTipoCuenta().getIdTipoCuenta());
            d.setTasaInteresAnual(e.getTasaInteresAnual());
            d.setFechaInicio(e.getFechaInicio());
            d.setFechaFin(e.getFechaFin());
            return d;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        TasaInteresHistorial t = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tasa not found: " + id));
        repository.delete(t);
    }
}