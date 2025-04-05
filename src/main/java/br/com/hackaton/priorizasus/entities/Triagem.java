package br.com.hackaton.priorizasus.entities;

import br.com.hackaton.priorizasus.enums.NivelPrioridade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private NivelPrioridade nivelPrioridade;

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

        if (maiorGravidade == 5) {
            this.nivelPrioridade = NivelPrioridade.VERMELHO;
        } else if (maiorGravidade == 4) {
            this.nivelPrioridade = NivelPrioridade.LARANJA;
        } else if (maiorGravidade == 3) {
            this.nivelPrioridade = NivelPrioridade.AMARELO;
        } else if (maiorGravidade == 2) {
            this.nivelPrioridade = NivelPrioridade.VERDE;
        } else {
            this.nivelPrioridade = NivelPrioridade.AZUL;
        }
    }
}
