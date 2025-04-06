package br.com.hackaton.priorizasus.dto;

import java.time.LocalDateTime;

public record PacienteParaCadastrarDTO(
        String nome,
        String cpf,
        LocalDateTime dataNascimento,
        String telefone,
        String endereco
) {}

