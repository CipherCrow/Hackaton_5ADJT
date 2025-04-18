package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.HistoricoAtendimentoDTO;
import br.com.hackaton.priorizasus.entities.AtendimentoHistorico;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.mapper.HistoricoAtendimentoMapper;
import br.com.hackaton.priorizasus.repository.AtendimentoHistoricoRepository;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarHistoricoAtendimentosUseCase {

    private final PacienteRepository pacienteRepository;
    private final AtendimentoHistoricoRepository atendimentoHistoricoRepository;

    public List<HistoricoAtendimentoDTO> executar(String login) {
        Paciente paciente = pacienteRepository.findByUsuario_Login(login)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Paciente não encontrado para o login informado"));

        List<AtendimentoHistorico> atendimentos = atendimentoHistoricoRepository.findByTriagem_Paciente_Id(paciente.getId());

        return atendimentos.stream()
                .map(HistoricoAtendimentoMapper::toDTO)
                .toList();
    }
}

