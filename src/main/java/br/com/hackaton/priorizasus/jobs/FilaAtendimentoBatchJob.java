package br.com.hackaton.priorizasus.jobs;

import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilaAtendimentoBatchJob {

    private final FilaAtendimentoRepository filaAtendimentoRepository;

    @Scheduled(fixedRate = 180000)// 3 minutos
    public void atualizarPesosPendentes() {
        List<FilaAtendimento> pendentes = filaAtendimentoRepository
                .findByStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE);

        for (FilaAtendimento fila : pendentes) {
            fila.atualizarPesoFila();
        }

        filaAtendimentoRepository.saveAll(pendentes);
        log.info("[BATCH] Pesos atualizados para {} atendimentos pendentes.", pendentes.size());
    }
}

