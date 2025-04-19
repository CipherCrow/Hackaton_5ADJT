package br.com.hackaton.priorizasus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RegistroChegadaDTO(
        @Schema(description = "Id do paciente cadastrado",
                example = "1")
        @NotNull(message = "ID do paciente é obrigatório")
        Long pacienteId,

        @Schema(description = "Indicador se será necessário uma triagem ou apenas é um atendimento administrativo",
                example = "true")
        @NotNull(message = "É necessário informar se o atendimento é administrativo")
        Boolean atendimentoAdministrativo
) {}

