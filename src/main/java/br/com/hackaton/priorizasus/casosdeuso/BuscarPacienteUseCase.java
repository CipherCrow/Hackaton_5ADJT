package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.PacienteCadastradoDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.mapper.PacienteMapper;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarPacienteUseCase {
    private final PacienteRepository pacienteRepository;

    public PacienteCadastradoDTO buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Paciente não encontrado"));
        return PacienteMapper.toDTO(paciente);
    }
}

