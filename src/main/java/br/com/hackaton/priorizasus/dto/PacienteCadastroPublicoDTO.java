package br.com.hackaton.priorizasus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDateTime;

public record PacienteCadastroPublicoDTO(
        @NotBlank(message = "Nome é obrigatório.")
        String nome,

        @NotBlank(message = "CPF é obrigatório.")
        String cpf,

        @Past(message = "Data de nascimento deve ser no passado.")
        LocalDateTime dataNascimento,

        String telefone,
        String endereco,

        @NotBlank(message = "Login é obrigatório.")
        String login,

        @NotBlank(message = "Senha é obrigatória.")
        String senha
) {}
