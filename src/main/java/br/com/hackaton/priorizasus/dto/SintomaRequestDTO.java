package br.com.hackaton.priorizasus.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SintomaRequestDTO (
        @NotBlank(message = "Deve existir uma descrição para o sintoma!")
        String descricao,
        @Min(value = 1,message = "Deve ter pelo menos 1 de gravidade!")
        @Max(value = 5,message = "Deve ter no maximo 5 de gravidade!")
        int gravidade
){}