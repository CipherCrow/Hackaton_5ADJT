package br.com.hackaton.priorizasus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfissionalSaudeRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        String crm,

        @NotBlank(message = "Especialidade é obrigatória")
        String especialidade,
        Long usuarioId
) {}