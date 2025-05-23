package br.com.hackaton.priorizasus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record PacienteCadastroPublicoDTO(
        @Schema(description = "Nome da pessoa para cadastro",
                example = "Julia Palito")
        @NotBlank(message = "Nome é obrigatório.")
        String nome,

        @Schema(description = "CPF da pessoa para cadastro",
                example = "12345678900")
        @NotBlank(message = "CPF é obrigatório.")
        String cpf,

        @Schema(description = "Data de nascimento da pessoa para cadastro",
                example = "2025-04-19")
        @Past(message = "Data de nascimento deve ser no passado.")
        LocalDate dataNascimento,

        @Schema(description = "Telefone para contato da pessoa para cadastro",
                example = "0066660315")
        String telefone,

        @Schema(description = "Endereço da pessoa para cadastro",
                example = "Rua das tamandujeiras, 123")
        String endereco,

        @Schema(description = "Login para ser usado no aplicativo",
                example = "JuPalito")
        @NotBlank(message = "Login é obrigatório.")
        String login,

        @Schema(description = "Senha para ser usada no aplicativo",
                example = "123456")
        @NotBlank(message = "Senha é obrigatória.")
        String senha
) {}
