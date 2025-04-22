package br.com.hackaton.priorizasus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfissionalSaudeRequestDTO(
        @Schema(description = "Nome do prossional de saúde",
                example = "Doutô Hause")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "Credencial de registro registro do profissional no respectivo Conselho Regional",
                example = "CRM/SP 123456")
        String crm,

        @Schema(description = "Area do Profissional",
                example = "Super Médico")
        @NotBlank(message = "Especialidade é obrigatória")
        String especialidade,
        Long usuarioId
) {}