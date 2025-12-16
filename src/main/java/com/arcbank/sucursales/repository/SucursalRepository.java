package com.arcbank.sucursales.repository;

import com.arcbank.sucursales.model.Sucursal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SucursalRepository extends MongoRepository<Sucursal, String> {

    // Buscar por código único de sucursal
    Optional<Sucursal> findByCodigoUnico(String codigoUnico);

    // Listar sucursales por nivel de ubicación
    List<Sucursal> findByUbicacionProvinciaNombre(String nombreProvincia);

    List<Sucursal> findByUbicacionCantonNombre(String nombreCanton);

    List<Sucursal> findByUbicacionParroquiaNombre(String nombreParroquia);
}
