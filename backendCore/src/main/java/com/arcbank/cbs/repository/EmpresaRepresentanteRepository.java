package com.arcbank.cbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arcbank.cbs.model.client.EmpresaRepresentante;


@Repository
public interface EmpresaRepresentanteRepository extends JpaRepository<EmpresaRepresentante, EmpresaRepresentante.EmpresaRepresentanteId> {

    List<EmpresaRepresentante> findByIdEmpresa(Integer idEmpresa);


    List<EmpresaRepresentante> findByIdRepresentante(Integer idPersona);


    List<EmpresaRepresentante> findByIdEmpresaAndEstado(Integer idEmpresa, String estado);


    List<EmpresaRepresentante> findByIdEmpresaAndRol(Integer idEmpresa, String rol);


    @Query("SELECT er FROM EmpresaRepresentante er " +
           "WHERE er.idEmpresa = :idEmpresa " +
           "AND er.idRepresentante = :idPersona " +
           "AND er.rol = :rol")
    Optional<EmpresaRepresentante> findByEmpresaAndPersonaAndRol(
            @Param("idEmpresa") Integer idEmpresa,
            @Param("idPersona") Integer idPersona,
            @Param("rol") String rol
    );


    @Query("SELECT er FROM EmpresaRepresentante er " +
           "WHERE er.idEmpresa = :idEmpresa " +
           "AND er.fechaFinDesignacion IS NULL")
    List<EmpresaRepresentante> findVigentesByEmpresa(@Param("idEmpresa") Integer idEmpresa);
}
