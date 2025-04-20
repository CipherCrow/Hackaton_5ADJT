package br.com.hackaton.priorizasus.dto;

import br.com.hackaton.priorizasus.entities.Sintoma;
import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record RealizarTriagemRequestDTO (
    @Schema(description = "ID do paciente cadastrado",
            example = "1")
    @NotNull(message = "Paciente ID é obrigatório")
     Long pacienteId,

    @Schema(description = "ID do profissional que realizou a triagem",
            example = "2000")
    @NotNull(message = "Profissional ID é obrigatório")
     Long profissionalId,

    @Schema(description = "Lista com os sintomas do paciente")
    @NotEmpty(message = "Deve haver pelo menos um sintoma")
    Set<Sintoma> sintomas,

    @Schema(description = "Se o doutor quiser definir a prioridade manualmente",
            example = "true")
    // Se o atendente optar por definir manualmente a prioridade
    Boolean prioridadeManual,

    @Schema(description = "Se o doutor optar por definir a prioridade manualmente, aqui deve ser especificado a prioridade.",
            allowableValues = {"VERMELHO", "LARANJA", "AMARELO", "VERDE", "AZUL"})
    // se for prioridade manual, ele pode enviar o nível desejado
    NivelPrioridadeEnum nivelPrioridadeManual
){}