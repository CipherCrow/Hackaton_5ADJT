package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.PacienteParaCadastrarDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarPacienteUseCase {

    private final PacienteRepository pacienteRepository;

    @Transactional
    public void atualizar(Long id, PacienteParaCadastrarDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() ->  new EntidadeNaoEncontradaException("Paciente não encontrado"));

        paciente.setNome(dto.nome());
        paciente.setTelefone(dto.telefone());
        paciente.setEndereco(dto.endereco());
        paciente.setDataNascimento(dto.dataNascimento());
        pacienteRepository.save(paciente);
    }
}

