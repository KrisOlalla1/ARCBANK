package com.arcbank.sucursales.service;

import com.arcbank.sucursales.dto.request.*;
import com.arcbank.sucursales.dto.response.*;
import com.arcbank.sucursales.exception.ResourceNotFoundException;
import com.arcbank.sucursales.model.*;
import com.arcbank.sucursales.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SucursalService {

    private final SucursalRepository sucursalRepo;

    @Transactional
    public SucursalDTO create(SucursalRequest request) {


        sucursalRepo.findByCodigoUnico(request.getCodigoUnico())
                .ifPresent(s -> { throw new IllegalArgumentException("codigoUnico ya existe: " + request.getCodigoUnico()); });

        Sucursal s = new Sucursal();
        s.setCodigoUnico(request.getCodigoUnico());
        s.setNombre(request.getNombre());
        s.setDireccion(request.getDireccion());
        s.setTelefono(request.getTelefono());
        s.setLatitud(request.getLatitud());
        s.setLongitud(request.getLongitud());
        s.setEstado(request.getEstado());
        s.setFechaApertura(request.getFechaApertura());


        s.setEntidadBancaria(toEntidad(request.getEntidadBancaria()));
        s.setUbicacion(toUbicacion(request.getUbicacion()));

        Sucursal saved = sucursalRepo.save(s);
        log.info("Sucursal creada: {}", saved.getCodigoUnico());
        return toDTO(saved);
    }

    public SucursalDTO findById(String id) {
        return sucursalRepo.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada: " + id));
    }

    public SucursalDTO findByCodigoUnico(String codigoUnico) {
        return sucursalRepo.findByCodigoUnico(codigoUnico)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada: " + codigoUnico));
    }

    public List<SucursalDTO> findAll() {
        return sucursalRepo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<SucursalDTO> findByProvincia(String provincia) {
        return sucursalRepo.findByUbicacionProvinciaNombre(provincia)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<SucursalDTO> findByCanton(String canton) {
        return sucursalRepo.findByUbicacionCantonNombre(canton)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<SucursalDTO> findByParroquia(String parroquia) {
        return sucursalRepo.findByUbicacionParroquiaNombre(parroquia)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public FeriadosPorNivelDTO getFeriados(String idSucursal) {
        Sucursal s = sucursalRepo.findById(idSucursal)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada: " + idSucursal));

        FeriadosPorNivel f = (s.getUbicacion() != null) ? s.getUbicacion().getFeriados() : null;
        if (f == null) {

            FeriadosPorNivelDTO dto = new FeriadosPorNivelDTO();
            dto.setProvincia(List.of());
            dto.setCanton(List.of());
            dto.setParroquia(List.of());
            return dto;
        }

        return toFeriadosDTO(f);
    }

    @Transactional
    public SucursalDTO update(String id, SucursalRequest request) {

        Sucursal s = sucursalRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada: " + id));


        if (request.getCodigoUnico() != null && !request.getCodigoUnico().equals(s.getCodigoUnico())) {
            sucursalRepo.findByCodigoUnico(request.getCodigoUnico())
                    .ifPresent(x -> { throw new IllegalArgumentException("codigoUnico ya existe: " + request.getCodigoUnico()); });
            s.setCodigoUnico(request.getCodigoUnico());
        }

        s.setNombre(request.getNombre());
        s.setDireccion(request.getDireccion());
        s.setTelefono(request.getTelefono());
        s.setLatitud(request.getLatitud());
        s.setLongitud(request.getLongitud());
        s.setEstado(request.getEstado());
        s.setFechaApertura(request.getFechaApertura());

        s.setEntidadBancaria(toEntidad(request.getEntidadBancaria()));
        s.setUbicacion(toUbicacion(request.getUbicacion()));

        log.info("Sucursal actualizada: {}", id);
        return toDTO(sucursalRepo.save(s));
    }

    @Transactional
    public void delete(String id) {
        Sucursal s = sucursalRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada: " + id));
        sucursalRepo.delete(s);
        log.info("Sucursal eliminada: {}", id);
    }

    // -------------------- MAPPERS --------------------

    private EntidadBancaria toEntidad(EntidadBancariaRequest r) {
        if (r == null) return null;
        EntidadBancaria e = new EntidadBancaria();
        e.setNombre(r.getNombre());
        e.setRuc(r.getRuc());
        e.setEstado(r.getEstado());
        return e;
    }

    private Ubicacion toUbicacion(UbicacionRequest r) {
        if (r == null) return null;

        Ubicacion u = new Ubicacion();
        u.setProvincia(toNivel(r.getProvincia()));
        u.setCanton(toNivel(r.getCanton()));
        u.setParroquia(toNivel(r.getParroquia()));
        u.setFeriados(toFeriados(r.getFeriados()));
        return u;
    }

    private NivelUbicacion toNivel(NivelUbicacionRequest r) {
        if (r == null) return null;
        NivelUbicacion n = new NivelUbicacion();
        n.setNombre(r.getNombre());
        n.setCodigo(r.getCodigo());
        return n;
    }

    private FeriadosPorNivel toFeriados(FeriadosPorNivelRequest r) {
        if (r == null) return null;

        FeriadosPorNivel f = new FeriadosPorNivel();
        f.setProvincia(r.getProvincia() != null ? r.getProvincia().stream().map(this::toFeriado).toList() : List.of());
        f.setCanton(r.getCanton() != null ? r.getCanton().stream().map(this::toFeriado).toList() : List.of());
        f.setParroquia(r.getParroquia() != null ? r.getParroquia().stream().map(this::toFeriado).toList() : List.of());
        return f;
    }

    private Feriado toFeriado(FeriadoRequest r) {
        if (r == null) return null;
        Feriado f = new Feriado();
        f.setFecha(r.getFecha());
        f.setDescripcion(r.getDescripcion());
        f.setTipoFeriado(r.getTipoFeriado());
        f.setActivo(r.getActivo());
        return f;
    }

    private SucursalDTO toDTO(Sucursal s) {
        SucursalDTO dto = new SucursalDTO();
        dto.setIdSucursal(s.getIdSucursal());
        dto.setCodigoUnico(s.getCodigoUnico());
        dto.setNombre(s.getNombre());
        dto.setDireccion(s.getDireccion());
        dto.setTelefono(s.getTelefono());
        dto.setLatitud(s.getLatitud());
        dto.setLongitud(s.getLongitud());
        dto.setEstado(s.getEstado());
        dto.setFechaApertura(s.getFechaApertura());

        dto.setEntidadBancaria(toEntidadDTO(s.getEntidadBancaria()));
        dto.setUbicacion(toUbicacionDTO(s.getUbicacion()));

        return dto;
    }

    private EntidadBancariaDTO toEntidadDTO(EntidadBancaria e) {
        if (e == null) return null;
        EntidadBancariaDTO dto = new EntidadBancariaDTO();
        dto.setNombre(e.getNombre());
        dto.setRuc(e.getRuc());
        dto.setEstado(e.getEstado());
        return dto;
    }

    private UbicacionDTO toUbicacionDTO(Ubicacion u) {
        if (u == null) return null;
        UbicacionDTO dto = new UbicacionDTO();
        dto.setProvincia(toNivelDTO(u.getProvincia()));
        dto.setCanton(toNivelDTO(u.getCanton()));
        dto.setParroquia(toNivelDTO(u.getParroquia()));
        dto.setFeriados(u.getFeriados() != null ? toFeriadosDTO(u.getFeriados()) : null);
        return dto;
    }

    private NivelUbicacionDTO toNivelDTO(NivelUbicacion n) {
        if (n == null) return null;
        NivelUbicacionDTO dto = new NivelUbicacionDTO();
        dto.setNombre(n.getNombre());
        dto.setCodigo(n.getCodigo());
        return dto;
    }

    private FeriadosPorNivelDTO toFeriadosDTO(FeriadosPorNivel f) {
        FeriadosPorNivelDTO dto = new FeriadosPorNivelDTO();
        dto.setProvincia(f.getProvincia() != null ? f.getProvincia().stream().map(this::toFeriadoDTO).toList() : List.of());
        dto.setCanton(f.getCanton() != null ? f.getCanton().stream().map(this::toFeriadoDTO).toList() : List.of());
        dto.setParroquia(f.getParroquia() != null ? f.getParroquia().stream().map(this::toFeriadoDTO).toList() : List.of());
        return dto;
    }

    private FeriadoDTO toFeriadoDTO(Feriado f) {
        if (f == null) return null;
        FeriadoDTO dto = new FeriadoDTO();
        dto.setFecha(f.getFecha());
        dto.setDescripcion(f.getDescripcion());
        dto.setTipoFeriado(f.getTipoFeriado());
        dto.setActivo(f.getActivo());
        return dto;
    }
}
