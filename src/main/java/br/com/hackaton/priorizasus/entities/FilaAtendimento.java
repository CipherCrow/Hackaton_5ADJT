package br.com.hackaton.priorizasus.entities;

import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private StatusAtendimentoEnum statusAtendimentoEnum;

    private LocalTime tempoEsperaEstimado;

    private int pesoFila;

    public void atualizarPesoFila() {
        long minutosEspera = Duration.between(horarioEntradaFila, LocalDateTime.now()).toMinutes();

        if (Boolean.TRUE.equals(atendimentoAdministrativo)) {
            // administrativo cresce devagar
            int pesoBase = 5;
            this.pesoFila = pesoBase + (int) (minutosEspera * 0.3);
        } else if (triagem != null && triagem.getNivelPrioridadeEnum() != null) {
            int prioridadePeso;
            double aceleradorTempo;
            switch (triagem.getNivelPrioridadeEnum()) {
                case VERMELHO -> {
                    prioridadePeso = 1000; // esse não entra na fila, mas por segurança
                    aceleradorTempo = 1; // cresce no mesmo ritmo, já tem prioridade máxima
                }
                case LARANJA -> {
                    prioridadePeso = 600;
                    aceleradorTempo = 0.9;
                }
                case AMARELO -> {
                    prioridadePeso = 40;
                    aceleradorTempo = 0.6;
                }
                case VERDE -> {
                    prioridadePeso = 20;
                    aceleradorTempo = 1.0;
                }
                case AZUL -> {
                    prioridadePeso = 10;
                    aceleradorTempo = 1.2;
                }
                default -> {
                    prioridadePeso = 0;
                    aceleradorTempo = 1.0;
                }
            }
            this.pesoFila = prioridadePeso + (int) (minutosEspera * aceleradorTempo);
        } else {
            this.pesoFila = (int) (minutosEspera * 0.5);
        }
    }

}

