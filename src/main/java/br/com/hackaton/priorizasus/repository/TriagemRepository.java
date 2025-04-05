package br.com.hackaton.priorizasus.repository;

import br.com.hackaton.priorizasus.entities.Triagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriagemRepository extends JpaRepository<Triagem, Long> {

}
