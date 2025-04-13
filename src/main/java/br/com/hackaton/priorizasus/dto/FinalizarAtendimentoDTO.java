package br.com.hackaton.priorizasus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FinalizarAtendimentoDTO(

        @NotNull(message = "Id do atendimento é obrigatório!")
        Long idAtendimento,

        @NotNull(message = "Id do profissional é obrigatório!")
        Long idProfissional,

        @NotBlank(message = "O Diagnostico é obrigatório!")
        String diagnostico,

        @NotBlank(message = "A prescricao é obrigatória!")
        String prescricao

) {}
