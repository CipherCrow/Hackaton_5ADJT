package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.ProfissionalSaudeDTO;
import br.com.hackaton.priorizasus.entities.ProfissionalSaude;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.mapper.ProfissionalSaudeMapper;
import br.com.hackaton.priorizasus.repository.ProfissionalSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarProfissionalPorIdUseCase {

    private final ProfissionalSaudeRepository repository;

    public ProfissionalSaudeDTO buscarPorId(Long id) {
        ProfissionalSaude profissional = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Profissional não encontrado"));
        return ProfissionalSaudeMapper.toDTO(profissional);
    }
}
