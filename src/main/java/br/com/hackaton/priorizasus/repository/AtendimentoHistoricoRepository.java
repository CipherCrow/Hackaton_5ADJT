package br.com.hackaton.priorizasus.repository;

import br.com.hackaton.priorizasus.entities.AtendimentoHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtendimentoHistoricoRepository extends JpaRepository<AtendimentoHistorico, Long> {
    List<AtendimentoHistorico> findByTriagem_Paciente_Id(Long id);
}
