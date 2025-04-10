package br.com.hackaton.priorizasus.dto;

import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;

import java.time.LocalDateTime;

public record FilaTriagemResponseDTO(
        Long id,
        String nomePaciente,
        String cpfPaciente,
        StatusTriagemEnum statusTriagem,
        LocalDateTime horarioEntrada
) {}
