package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.ProfissionalSaudeDTO;
import br.com.hackaton.priorizasus.mapper.ProfissionalSaudeMapper;
import br.com.hackaton.priorizasus.repository.ProfissionalSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarTodosProfissionaisUseCase {

    private final ProfissionalSaudeRepository repository;

    public List<ProfissionalSaudeDTO> buscarTodos() {
        return repository.findAll()
                .stream()
                .map(ProfissionalSaudeMapper::toDTO)
                .toList();
    }
}