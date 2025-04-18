package br.com.hackaton.priorizasus.entities;

import br.com.hackaton.priorizasus.enums.PermissaoEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 50)
    private String login;

    @NotBlank
    @Size(min = 8)
    private String senha;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PermissaoEnum permissao;
}