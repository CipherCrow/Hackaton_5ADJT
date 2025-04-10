package br.com.hackaton.priorizasus.dto;

import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusFilaTriagemDTO(
        @NotNull(message = "Status da triagem é obrigatório")
        StatusTriagemEnum statusTriagem
) {}
