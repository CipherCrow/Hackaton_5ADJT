package br.com.hackaton.priorizasus.repository;

import br.com.hackaton.priorizasus.entities.ProfissionalSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude, Long> {

}
