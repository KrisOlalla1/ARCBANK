package com.arcbank.cbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arcbank.cbs.model.client.EmpresaRepresentante;

/**
 * Repositorio para la entidad EmpresaRepresentante.
 * NOTE: La tabla tiene PK compuesta (IdEmpresa, IdRepresentante) por eso el ID del repositorio
 * es la clase `EmpresaRepresentante.EmpresaRepresentanteId`.
 */
@Repository
public interface EmpresaRepresentanteRepository extends JpaRepository<EmpresaRepresentante, EmpresaRepresentante.EmpresaRepresentanteId> {
    /**
     * Busca todos los representantes de una empresa por la columna IdEmpresa.
     */
    List<EmpresaRepresentante> findByIdEmpresa(Integer idEmpresa);

    /**
     * Busca todas las representaciones de una persona por la columna IdRepresentante.
     */
    List<EmpresaRepresentante> findByIdRepresentante(Integer idPersona);

    /**
     * Busca representantes activos de una empresa.
     */
    List<EmpresaRepresentante> findByIdEmpresaAndEstado(Integer idEmpresa, String estado);

    /**
     * Busca representantes de una empresa con un rol específico.
     */
    List<EmpresaRepresentante> findByIdEmpresaAndRol(Integer idEmpresa, String rol);

    /**
     * Busca una representación específica por empresa, persona y rol.
     */
    @Query("SELECT er FROM EmpresaRepresentante er " +
           "WHERE er.idEmpresa = :idEmpresa " +
           "AND er.idRepresentante = :idPersona " +
           "AND er.rol = :rol")
    Optional<EmpresaRepresentante> findByEmpresaAndPersonaAndRol(
            @Param("idEmpresa") Integer idEmpresa,
            @Param("idPersona") Integer idPersona,
            @Param("rol") String rol
    );

    /**
     * Busca representantes vigentes (con FechaFinDesignacion NULL).
     */
    @Query("SELECT er FROM EmpresaRepresentante er " +
           "WHERE er.idEmpresa = :idEmpresa " +
           "AND er.fechaFinDesignacion IS NULL")
    List<EmpresaRepresentante> findVigentesByEmpresa(@Param("idEmpresa") Integer idEmpresa);
}
