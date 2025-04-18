package br.com.hackaton.priorizasus.dto;

public record HistoricoAtendimentoDTO(
        String dataAtendimento,
        String profissional,
        String sintomas,
        String diagnostico,
        String prescricao,
        String prioridade
) {}

