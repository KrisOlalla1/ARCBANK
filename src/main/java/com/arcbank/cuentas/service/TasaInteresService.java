package com.arcbank.cuentas.service;

import com.arcbank.cuentas.dto.TasaInteresHistorialDTO;

import java.util.List;

public interface TasaInteresService {
    TasaInteresHistorialDTO create(TasaInteresHistorialDTO dto);
    TasaInteresHistorialDTO findById(Integer id);
    List<TasaInteresHistorialDTO> findAll();
    void delete(Integer id);
}
