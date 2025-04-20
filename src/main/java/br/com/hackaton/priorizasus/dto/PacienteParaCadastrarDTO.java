package br.com.hackaton.priorizasus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDateTime;

public record PacienteParaCadastrarDTO(

        @Schema(description = "Nome do paciente", example = "Pedro Paulo Junior")
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        @Schema(description = "CPF do paciente", example = "12345678902")
        @NotBlank(message = "CPF é obrigatório")
        String cpf,
        @Schema(description = "Data de nascimento do paciente",example = "1999-01-01T00:00:00")
        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message="Data de nascimento deve estar no passado")
        LocalDateTime dataNascimento,
        @Schema(description = "Telefone do paciente",example = "11999990009")
        String telefone,
        @Schema(description = "Endereco do paciente",example = "Rua Julia Ramos, 123")
        String endereco
) {}

