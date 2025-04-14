package br.com.hackaton.priorizasus.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Deve ser inserido um usuario!")
        String usuario,
        @NotBlank(message = "Deve ser inserido uma senha!")
        String senha
) {}