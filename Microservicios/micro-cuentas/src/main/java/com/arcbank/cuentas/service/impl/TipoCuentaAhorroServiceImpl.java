package com.arcbank.cuentas.service.impl;

import com.arcbank.cuentas.dto.TipoCuentaAhorroDTO;
import com.arcbank.cuentas.exception.ResourceNotFoundException;
import com.arcbank.cuentas.models.TipoCuentaAhorro;
import com.arcbank.cuentas.repository.TipoCuentaAhorroRepository;
import com.arcbank.cuentas.service.TipoCuentaAhorroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoCuentaAhorroServiceImpl implements TipoCuentaAhorroService {

    private final TipoCuentaAhorroRepository repository;

    @Override
    public TipoCuentaAhorroDTO crear(TipoCuentaAhorroDTO dto) {
        TipoCuentaAhorro entity = toEntity(dto);
        entity.setIdTipoCuenta(null);
        return toDto(repository.save(entity));
    }

    @Override
    public List<TipoCuentaAhorroDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TipoCuentaAhorroDTO obtenerPorId(Integer id) {
        TipoCuentaAhorro entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoCuentaAhorro no encontrado: " + id));
        return toDto(entity);
    }

    @Override
    public TipoCuentaAhorroDTO actualizar(Integer id, TipoCuentaAhorroDTO dto) {
        TipoCuentaAhorro entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoCuentaAhorro no encontrado: " + id));

        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setTasaInteresMaxima(dto.getTasaInteresMaxima());
        entity.setAmortizacion(dto.getAmortizacion());
        entity.setActivo(dto.getActivo());

        return toDto(repository.save(entity));
    }

    @Override
    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("TipoCuentaAhorro no encontrado: " + id);
        }
        repository.deleteById(id);
    }

    // ====== Mappers ======
    private TipoCuentaAhorroDTO toDto(TipoCuentaAhorro e) {
        return TipoCuentaAhorroDTO.builder()
                .idTipoCuenta(e.getIdTipoCuenta())
                .nombre(e.getNombre())
                .descripcion(e.getDescripcion())
                .tasaInteresMaxima(e.getTasaInteresMaxima())
                .amortizacion(e.getAmortizacion())
                .activo(e.getActivo())
                .build();
    }

    private TipoCuentaAhorro toEntity(TipoCuentaAhorroDTO d) {
        return TipoCuentaAhorro.builder()
                .idTipoCuenta(d.getIdTipoCuenta())
                .nombre(d.getNombre())
                .descripcion(d.getDescripcion())
                .tasaInteresMaxima(d.getTasaInteresMaxima())
                .amortizacion(d.getAmortizacion())
                .activo(d.getActivo() != null ? d.getActivo() : true)
                .build();
    }
}
