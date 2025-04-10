package br.com.hackaton.priorizasus.repository;

import br.com.hackaton.priorizasus.entities.FilaTriagem;
import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilaTriagemRepository extends JpaRepository<FilaTriagem, Long> {
    Optional<FilaTriagem> findByPacienteCpf(String cpf);
    List<FilaTriagem> findTop10ByStatusTriagemOrderByHorarioEntradaAsc(StatusTriagemEnum statusTriagem);
}
