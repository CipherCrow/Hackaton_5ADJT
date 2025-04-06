package br.com.hackaton.priorizasus.repository;

import br.com.hackaton.priorizasus.entities.FilaTriagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilaTriagemRepository extends JpaRepository<FilaTriagem, Long> {

}
