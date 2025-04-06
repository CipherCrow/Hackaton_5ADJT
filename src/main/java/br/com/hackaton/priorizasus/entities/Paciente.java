package br.com.hackaton.priorizasus.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String nome;

    @NotBlank
    @Digits(integer = 8, fraction = 2)
    private String cpf;

    @Past
    private LocalDateTime dataNascimento;

    @NotBlank
    private String telefone;

    @NotBlank
    private String endereco;

    @OneToOne
    private Usuario usuario;
}