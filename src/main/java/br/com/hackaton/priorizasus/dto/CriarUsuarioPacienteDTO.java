package br.com.hackaton.priorizasus.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarUsuarioPacienteDTO(
        @NotBlank(message = "Login é obrigatório!")
        String login,
        @NotBlank(message = "Senha é obrigatória!")
        String senha,
        @NotBlank(message = "Cpf é obrigatório!")
        String cpf
) {}