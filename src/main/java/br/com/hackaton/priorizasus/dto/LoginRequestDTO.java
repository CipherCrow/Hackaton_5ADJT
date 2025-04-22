package br.com.hackaton.priorizasus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Schema(description = "Nome de usuário usado no cadastro",
                example = "superPaciente")
        @NotBlank(message = "Deve ser inserido um usuario!")
        String usuario,
        @Schema(description = "Senha usada no cadastro",
                example = "123456")
        @NotBlank(message = "Deve ser inserido uma senha!")
        String senha
) {}