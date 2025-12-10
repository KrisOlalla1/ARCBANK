package com.arcbank.cuentas.service;

import com.arcbank.cuentas.dto.TipoCuentaAhorroDTO;

import java.util.List;

public interface TipoCuentaService {
    TipoCuentaAhorroDTO create(TipoCuentaAhorroDTO dto);
    TipoCuentaAhorroDTO update(Integer id, TipoCuentaAhorroDTO dto);
    TipoCuentaAhorroDTO findById(Integer id);
    List<TipoCuentaAhorroDTO> findAll();
    void delete(Integer id);
}