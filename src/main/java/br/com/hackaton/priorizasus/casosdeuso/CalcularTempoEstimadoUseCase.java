package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalcularTempoEstimadoUseCase {

    private final FilaAtendimentoRepository filaAtendimentoRepository;

    public LocalTime calcularTempoEstimado(FilaAtendimento filaAtual) {
        List<FilaAtendimento> filaPendente = filaAtendimentoRepository
                .findByStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE);

        long pacientesNaFrente = filaPendente.stream()
                .filter(f -> !f.getId().equals(filaAtual.getId())) // ignorar ele mesmo
                .filter(f -> f.getPesoFila() > filaAtual.getPesoFila() ||
                        (f.getPesoFila() == filaAtual.getPesoFila() &&
                                f.getHorarioEntradaFila().isBefore(filaAtual.getHorarioEntradaFila())))
                .count();

        int tempoMedioMin = 10;
        int minutos = (int) (pacientesNaFrente * tempoMedioMin);

        return LocalTime.of(minutos / 60, minutos % 60);
    }
}

