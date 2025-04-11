package br.com.hackaton.priorizasus.dto;

import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;

import java.time.LocalDateTime;

public record TriagemResponseDTO(
        Long triagemId,
        NivelPrioridadeEnum nivelPrioridadeEnum,
        LocalDateTime dataTriagem
){}