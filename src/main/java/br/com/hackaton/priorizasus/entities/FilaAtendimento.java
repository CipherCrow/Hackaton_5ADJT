package br.com.hackaton.priorizasus.entities;

import br.com.hackaton.priorizasus.enums.StatusAtendimento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilaAtendimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Triagem triagem;

    @NotNull
    private Boolean atendimentoAdministrativo;

    @PastOrPresent
    private LocalDateTime horarioEntradaFila;

    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusAtendimento statusAtendimento;

    private int pesoFila;

    public void atualizarPesoFila() {
        long minutosEspera = java.time.Duration.between(horarioEntradaFila, LocalDateTime.now()).toMinutes();

        if (Boolean.TRUE.equals(atendimentoAdministrativo)) {
            int pesoBase = 5;
            this.pesoFila = pesoBase + (int) (minutosEspera * 0.5);
        } else if (triagem != null && triagem.getNivelPrioridade() != null) {
            int prioridadePeso;
            switch (triagem.getNivelPrioridade()) {
                case VERMELHO -> prioridadePeso = 100;
                case LARANJA -> prioridadePeso = 70;
                case AMARELO -> prioridadePeso = 40;
                case VERDE -> prioridadePeso = 20;
                case AZUL -> prioridadePeso = 10;
                default -> prioridadePeso = 0;
            }
            this.pesoFila = prioridadePeso + (int) (minutosEspera * 0.5);
        } else {
            this.pesoFila = (int) (minutosEspera * 0.5);
        }
    }
}

