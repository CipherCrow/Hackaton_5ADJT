package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.entities.FilaTriagem;
import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.FilaTriagemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlterarStatusFilaTriagemUseCase {

    private final FilaTriagemRepository filaTriagemRepository;

    @Transactional
    public void alterarStatus(Long id, StatusTriagemEnum novoStatus) {
        FilaTriagem fila = filaTriagemRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Paciente na fila de triagem não encontrado."));

        fila.setStatusTriagem(novoStatus);
        filaTriagemRepository.save(fila);
    }
}



