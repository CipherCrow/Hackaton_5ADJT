package br.com.hackaton.priorizasus.repository;

import br.com.hackaton.priorizasus.entities.AtendimentoHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtendimentoHistoricoRepository extends JpaRepository<AtendimentoHistorico, Long> {

}
