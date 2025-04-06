package br.com.hackaton.priorizasus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDateTime;

public record PacienteParaCadastrarDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,
        @NotBlank(message = "CPF é obrigatório")
        String cpf,
        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message="Data de nascimento deve estar no passado")
        LocalDateTime dataNascimento,
        String telefone,
        String endereco
) {}

