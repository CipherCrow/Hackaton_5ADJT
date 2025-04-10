package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.SintomaRequestDTO;
import br.com.hackaton.priorizasus.entities.Sintoma;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.SintomaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarSintomaUseCase {

    private final SintomaRepository sintomaRepository;

    @Transactional
    public void atualizar(Long id, SintomaRequestDTO dto) {
        Sintoma sintoma = sintomaRepository.findById(id)
                .orElseThrow(() ->  new EntidadeNaoEncontradaException("Sintoma não encontrado"));

        sintoma.setDescricao(dto.descricao());
        sintoma.setGravidade(dto.gravidade());
        sintomaRepository.save(sintoma);
    }
}

