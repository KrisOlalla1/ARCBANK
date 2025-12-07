package ec.arcbank.micro.interbancario.repository;

import ec.arcbank.micro.interbancario.model.LogHermes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogHermesRepository extends JpaRepository<LogHermes, Integer> {

}
