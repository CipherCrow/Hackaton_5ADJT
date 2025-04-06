package br.com.hackaton.priorizasus.dto;

public record SintomaDTO(
        Long id,
        String descricao,
        int gravidade
){}