package br.com.hackaton.priorizasus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FinalizarAtendimentoDTO(

        @Schema(description = "O código do atendimento que está sendo atendido pelo profissional.",
                example = "2")
        @NotNull(message = "Id do atendimento é obrigatório!")
        Long idAtendimento,

        @Schema(description = "O id do proprio profissional que está fazendo o atendimento.",
                example = "2000")
        @NotNull(message = "Id do profissional é obrigatório!")
        Long idProfissional,

        @Schema(description = "O diagnóstico para o paciente.",
                example = "Ressaca pós carnaval")
        @NotBlank(message = "O Diagnostico é obrigatório!")
        String diagnostico,

        @Schema(description = "Prescição médica para ajudar com o Diagnóstico",
                example = "Muita hidratação!!!!")
        @NotBlank(message = "A prescricao é obrigatória!")
        String prescricao
) {}
