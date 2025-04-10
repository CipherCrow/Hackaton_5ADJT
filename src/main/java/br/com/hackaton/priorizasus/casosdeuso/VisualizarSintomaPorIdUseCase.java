package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.SintomaDTO;
import br.com.hackaton.priorizasus.entities.Sintoma;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.mapper.SintomaMapper;
import br.com.hackaton.priorizasus.repository.SintomaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VisualizarSintomaPorIdUseCase {

    private final SintomaRepository sintomaRepository;

    public SintomaDTO buscarSintoma(Long id) {
        Sintoma sintoma = sintomaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Sintoma não encontrado"));
        return SintomaMapper.toDTO(sintoma);
    }

}


