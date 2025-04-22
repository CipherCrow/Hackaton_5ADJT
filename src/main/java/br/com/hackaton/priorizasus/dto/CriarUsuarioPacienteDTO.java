package br.com.hackaton.priorizasus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CriarUsuarioPacienteDTO(
        @Schema(description = "Login para o usuário",
                example = "superPaciente")
        @NotBlank(message = "Login é obrigatório!")
        String login,
        @Schema(description = "Senha do usuário",
                example = "123456")
        @NotBlank(message = "Senha é obrigatória!")
        String senha,
        @Schema(description = "Cpf do paciente já cadastrado",
                example = "12345678902")
        @NotBlank(message = "Cpf é obrigatório!")
        String cpf
) {}