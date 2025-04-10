package br.com.hackaton.priorizasus.repository;

import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilaAtendimentoRepository extends JpaRepository<FilaAtendimento, Long> {

}
