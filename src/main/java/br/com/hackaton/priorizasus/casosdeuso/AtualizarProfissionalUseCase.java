package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.ProfissionalSaudeRequestDTO;
import br.com.hackaton.priorizasus.entities.ProfissionalSaude;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.ProfissionalSaudeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarProfissionalUseCase {

    private final ProfissionalSaudeRepository repository;

    @Transactional
    public void atualizar(Long id, ProfissionalSaudeRequestDTO dto) {
        ProfissionalSaude profissional = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Profissional não encontrado"));

        profissional.setNome(dto.nome());
        profissional.setCrm(dto.crm());
        profissional.setEspecialidade(dto.especialidade());
        repository.save(profissional);
    }
}

