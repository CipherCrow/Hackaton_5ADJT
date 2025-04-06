package br.com.hackaton.priorizasus.dto;

import jakarta.validation.constraints.NotNull;

public record RegistroChegadaDTO(
        @NotNull(message = "ID do paciente é obrigatório")
        Long pacienteId,

        @NotNull(message = "É necessário informar se o atendimento é administrativo")
        Boolean atendimentoAdministrativo
) {}

