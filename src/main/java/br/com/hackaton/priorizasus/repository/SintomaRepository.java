package br.com.hackaton.priorizasus.repository;

import br.com.hackaton.priorizasus.entities.Sintoma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SintomaRepository extends JpaRepository<Sintoma, Long> {

}
