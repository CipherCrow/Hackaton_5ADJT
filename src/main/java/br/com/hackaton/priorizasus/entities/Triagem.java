package br.com.hackaton.priorizasus.entities;

import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Triagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Paciente paciente;

    @ManyToMany
    @NotNull
    private Set<Sintoma> sintomas;

    @Enumerated(EnumType.STRING)
    @NotNull
    private NivelPrioridadeEnum nivelPrioridadeEnum;

    @PastOrPresent
    private LocalDateTime dataTriagem;

    @ManyToOne
    @NotNull
    private ProfissionalSaude profissional;

    private Boolean prioridadeManual;

    public void calcularNivelPrioridade() {
        if (Boolean.TRUE.equals(prioridadeManual)) {
            return;
        }

        int maiorGravidade = sintomas.stream()
                .mapToInt(Sintoma::getGravidade)
                .max()
                .orElse(1);

        switch (maiorGravidade) {
            case (5):
                this.nivelPrioridadeEnum = NivelPrioridadeEnum.VERMELHO;
                break;
            case (4):
                this.nivelPrioridadeEnum = NivelPrioridadeEnum.LARANJA;
                break;
            case (3):
                this.nivelPrioridadeEnum = NivelPrioridadeEnum.AMARELO;
                break;
            case (2):
                this.nivelPrioridadeEnum = NivelPrioridadeEnum.VERDE;
                break;
            default:
                this.nivelPrioridadeEnum = NivelPrioridadeEnum.AZUL;
                break;
        }

    }
}
