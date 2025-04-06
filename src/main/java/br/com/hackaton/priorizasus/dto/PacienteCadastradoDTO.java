package br.com.hackaton.priorizasus.dto;

public record PacienteCadastradoDTO(
        Long pacienteId,
        String nomePaciente,
        String cpfPaciente
) {}

