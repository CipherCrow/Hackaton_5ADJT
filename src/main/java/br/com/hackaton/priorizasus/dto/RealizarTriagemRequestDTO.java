package br.com.hackaton.priorizasus.dto;

import br.com.hackaton.priorizasus.entities.Sintoma;
import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record RealizarTriagemRequestDTO (
    @NotNull(message = "Paciente ID é obrigatório")
     Long pacienteId,

    @NotNull(message = "Profissional ID é obrigatório")
     Long profissionalId,

    @NotEmpty(message = "Deve haver pelo menos um sintoma")
    Set<Sintoma> sintomas,

    // Se o atendente optar por definir manualmente a prioridade
    Boolean prioridadeManual,

    // se for prioridade manual, ele pode enviar o nível desejado
    NivelPrioridadeEnum nivelPrioridadeManual
){}