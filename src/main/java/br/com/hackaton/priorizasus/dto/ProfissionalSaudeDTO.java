package br.com.hackaton.priorizasus.dto;

public record ProfissionalSaudeDTO(
        Long id,
        String nome,
        String crm,
        String especialidade
) {}