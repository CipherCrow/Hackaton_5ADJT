package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.RealizarTriagemRequestDTO;
import br.com.hackaton.priorizasus.dto.TriagemResponseDTO;
import br.com.hackaton.priorizasus.entities.*;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RealizarTriagemUseCase {

    private final PacienteRepository pacienteRepository;
    private final ProfissionalSaudeRepository profissionalRepository;
    private final TriagemRepository triagemRepository;
    private final FilaAtendimentoRepository filaAtendimentoRepository;
    private final FilaTriagemRepository filaTriagemRepository;
    private final CalcularTempoEstimadoUseCase calcularTempoEstimadoUseCase;
    private final VisualizarSintomaPorIdUseCase visualizarSintomaPorIdUseCase;

    @Transactional
    public TriagemResponseDTO realizarTriagem(RealizarTriagemRequestDTO dto) {
        // Buscando os dados do paciente e do profissional
        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Paciente não encontrado"));
        ProfissionalSaude profissional = profissionalRepository.findById(dto.profissionalId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Profissional não encontrado"));

        Triagem triagem = new Triagem();
        triagem.setPaciente(paciente);
        triagem.setProfissional(profissional);

        Set<Sintoma> sintomasEncontrados =  dto.sintomas().stream()
                .map(s -> visualizarSintomaPorIdUseCase.buscarSintoma(s.getId())
                        )
                .map(s -> Sintoma.builder().id(s.id()).gravidade(s.gravidade()).descricao(s.descricao()).build())
                .collect(Collectors.toSet());

        triagem.setSintomas(sintomasEncontrados);
        triagem.setDataTriagem(LocalDateTime.now());
        // Se a prioridade for manual, usamos o valor enviado; senão, calculamos automaticamente
        if (Boolean.TRUE.equals(dto.prioridadeManual()) && dto.nivelPrioridadeManual() != null) {
            triagem.setPrioridadeManual(true);
            triagem.setNivelPrioridadeEnum(dto.nivelPrioridadeManual());
        } else {
            triagem.setPrioridadeManual(false);
            triagem.calcularNivelPrioridade();
        }
        Triagem triagemSalva = triagemRepository.save(triagem);

        // Cria a entrada na FilaAtendimento (peso inicial baseado em tempo de espera = 0)
        FilaAtendimento filaAtendimento = new FilaAtendimento();
        filaAtendimento.setTriagem(triagemSalva);
        filaAtendimento.setAtendimentoAdministrativo(false);
        filaAtendimento.setHorarioEntradaFila(LocalDateTime.now());  // tempo de espera inicial = 0, mas pode aumentar com o tempo
        filaAtendimento.setStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE);
        // O método atualizarPesoFila fará o cálculo usando o tempo de espera e a prioridade
        filaAtendimento.atualizarPesoFila();

        LocalTime tempoEstimado = calcularTempoEstimadoUseCase.calcularTempoEstimado(filaAtendimento);
        filaAtendimento.setTempoEsperaEstimado(tempoEstimado);
        filaAtendimentoRepository.save(filaAtendimento);

        // Atualiza o registro na FilaTriagem para TRIAGEM_REALIZADA
        FilaTriagem filaTriagem = filaTriagemRepository.findByPacienteIdAndStatusTriagem(paciente.getId(),StatusTriagemEnum.EM_ANDAMENTO)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Paciente não encontrado na fila de triagem"));
        filaTriagem.setStatusTriagem(StatusTriagemEnum.TRIAGEM_REALIZADA);
        filaTriagemRepository.save(filaTriagem);

        // Retorna o DTO de resposta
        return new TriagemResponseDTO(triagemSalva.getId(), triagemSalva.getNivelPrioridadeEnum(), triagemSalva.getDataTriagem());
    }
}


