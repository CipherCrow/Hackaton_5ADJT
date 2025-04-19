package br.com.hackaton.priorizasus.repository;

import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilaAtendimentoRepository extends JpaRepository<FilaAtendimento, Long> {

    List<FilaAtendimento> findByStatusAtendimentoEnum(StatusAtendimentoEnum statusAtendimento);
    Optional<FilaAtendimento> findByTriagem_Paciente_CpfAAndStatusAtendimentoEnum(String cpf,StatusAtendimentoEnum statusAtendimento);
}
