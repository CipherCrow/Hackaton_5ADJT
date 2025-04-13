package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.FinalizarAtendimentoDTO;
import br.com.hackaton.priorizasus.entities.AtendimentoHistorico;
import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.entities.ProfissionalSaude;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.AtendimentoHistoricoRepository;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import br.com.hackaton.priorizasus.repository.ProfissionalSaudeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FinalizarAtendimentoUseCase {

    private final FilaAtendimentoRepository filaAtendimentoRepository;
    private final AtendimentoHistoricoRepository atendimentoHistoricoRepository;
    private final ProfissionalSaudeRepository profissionalSaudeRepository;

    @Transactional
    public void executar(FinalizarAtendimentoDTO dto) {
        FilaAtendimento fila = filaAtendimentoRepository.findById(dto.idAtendimento())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Atendimento não encontrado"));

        if (!StatusAtendimentoEnum.PENDENTE.equals(fila.getStatusAtendimentoEnum())) {
            throw new IllegalArgumentException("Apenas atendimentos pendentes podem ser finalizados");
        }

        ProfissionalSaude profissional = profissionalSaudeRepository.findById(dto.idProfissional())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Profissional não encontrado"));

        fila.setStatusAtendimentoEnum(StatusAtendimentoEnum.CONCLUIDO);
        filaAtendimentoRepository.save(fila);

        AtendimentoHistorico historico = new AtendimentoHistorico();
        historico.setTriagem(fila.getTriagem());
        historico.setDiagnostico(dto.diagnostico());
        historico.setPrescricao(dto.prescricao());
        historico.setDataAtendimento(LocalDateTime.now());
        historico.setProfissional(profissional);

        atendimentoHistoricoRepository.save(historico);
    }
}



