package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarStatusAtendimentoUseCase {

    private final FilaAtendimentoRepository filaAtendimentoRepository;

    @Transactional
    public void executar(Long id, StatusAtendimentoEnum novoStatus) {
        FilaAtendimento fila = filaAtendimentoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fila não encontrada"));
        fila.setStatusAtendimentoEnum(novoStatus);
        filaAtendimentoRepository.save(fila);
    }
}


