package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.ProfissionalSaudeDTO;
import br.com.hackaton.priorizasus.dto.ProfissionalSaudeRequestDTO;
import br.com.hackaton.priorizasus.entities.ProfissionalSaude;
import br.com.hackaton.priorizasus.mapper.ProfissionalSaudeMapper;
import br.com.hackaton.priorizasus.repository.ProfissionalSaudeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarProfissionalUseCase {

    private final ProfissionalSaudeRepository repository;

    @Transactional
    public ProfissionalSaudeDTO cadastrar(ProfissionalSaudeRequestDTO dto) {
        ProfissionalSaude entity = ProfissionalSaudeMapper.toEntity(dto);
        ProfissionalSaude salvo = repository.save(entity);
        return ProfissionalSaudeMapper.toDTO(salvo);
    }
}


