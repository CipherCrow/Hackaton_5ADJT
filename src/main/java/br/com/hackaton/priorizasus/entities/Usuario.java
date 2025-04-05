package br.com.hackaton.priorizasus.entities;

import br.com.hackaton.priorizasus.enums.PermissaoEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 50)
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PermissaoEnum permissao;
}