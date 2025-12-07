package ec.arcbank.micro.interbancario.repository;

import ec.arcbank.micro.interbancario.model.OrdenInterbancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenInterbancariaRepository extends JpaRepository<OrdenInterbancaria, Integer> {

}
