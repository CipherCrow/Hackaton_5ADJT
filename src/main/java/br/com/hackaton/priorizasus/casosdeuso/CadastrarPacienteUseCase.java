package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.PacienteCadastradoDTO;
import br.com.hackaton.priorizasus.dto.PacienteParaCadastrarDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.mapper.PacienteMapper;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarPacienteUseCase {
    private final PacienteRepository pacienteRepository;

    @Transactional
    public PacienteCadastradoDTO cadastrar(PacienteParaCadastrarDTO dto) {
        Paciente paciente = PacienteMapper.toEntity(dto);
        Paciente salvo = pacienteRepository.save(paciente);
        return PacienteMapper.toDTO(salvo);
    }
}


