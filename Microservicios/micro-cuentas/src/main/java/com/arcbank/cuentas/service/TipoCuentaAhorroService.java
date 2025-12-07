package com.arcbank.cuentas.service;

import com.arcbank.cuentas.dto.TipoCuentaAhorroDTO;

import java.util.List;

public interface TipoCuentaAhorroService {

    TipoCuentaAhorroDTO crear(TipoCuentaAhorroDTO dto);

    List<TipoCuentaAhorroDTO> listar();

    TipoCuentaAhorroDTO obtenerPorId(Integer id);

    TipoCuentaAhorroDTO actualizar(Integer id, TipoCuentaAhorroDTO dto);

    void eliminar(Integer id);
}
