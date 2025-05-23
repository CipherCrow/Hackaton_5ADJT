package br.com.hackaton.priorizasus.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtendimentoHistorico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Triagem triagem;

    @NotBlank
    private String diagnostico;

    @NotBlank
    private String prescricao;

    @PastOrPresent
    private LocalDateTime dataAtendimento;

    @ManyToOne
    @NotNull
    private ProfissionalSaude profissional;
}
