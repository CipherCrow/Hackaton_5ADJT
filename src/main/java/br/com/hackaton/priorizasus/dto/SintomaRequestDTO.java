package br.com.hackaton.priorizasus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SintomaRequestDTO (
        @Schema(description = "Descrição do sintoma", example = "Muita dor de cabeça no globulo Central 25B")
        @NotBlank(message = "Deve existir uma descrição para o sintoma!")
        String descricao,
        @Schema(description = "Nivel de gravidade do sintoma",
                example = "2",
                allowableValues = {"1","2","3","4","5"})
        @Min(value = 1,message = "Deve ter pelo menos 1 de gravidade!")
        @Max(value = 5,message = "Deve ter no maximo 5 de gravidade!")
        int gravidade
){}