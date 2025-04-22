package br.com.hackaton.priorizasus.dto;

import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record FilaAtendimentoDTO(
        Long idAtendimento,
        String nomePaciente,
        NivelPrioridadeEnum nivelPrioridade,
        LocalDateTime horarioEntrada,
        LocalTime tempoEstimadoEspera,
        StatusAtendimentoEnum status
) {}
