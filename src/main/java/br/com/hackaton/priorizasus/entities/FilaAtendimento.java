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
        double tempoCrescimento = Math.log(minutosEspera + 1); // Evita log(0)

        if (Boolean.TRUE.equals(atendimentoAdministrativo)) {
            this.pesoFila = 5 + (int) (tempoCrescimento * 2.0);
        } else if (triagem != null && triagem.getNivelPrioridadeEnum() != null) {
            int prioridadePeso;
            double aceleradorTempo;
            switch (triagem.getNivelPrioridadeEnum()) {
                case VERMELHO -> {
                    prioridadePeso = 1000;
                    aceleradorTempo = 1;
                }
                case LARANJA -> {
                    prioridadePeso = 600;
                    aceleradorTempo = 0.5;
                }
                case AMARELO -> {
                    prioridadePeso = 80;            //Inicia com 80
                    aceleradorTempo = 4.81;        // para atingir ~105 em 180 min
                }
                case VERDE -> {
                    prioridadePeso = 40;            // inicia com 40
                    aceleradorTempo = 11.5;        // para atingir ~100 em 180 min
                }
                case AZUL -> {
                    prioridadePeso = 20;            // inicia com 20
                    aceleradorTempo = 13.3;        // para atingir ~80 em 90 min
                }
                default -> {
                    prioridadePeso = 0;
                    aceleradorTempo = 1.0;
                }
            }
            this.pesoFila = prioridadePeso + (int) (tempoCrescimento * aceleradorTempo);
        } else {
            this.pesoFila = (int) (tempoCrescimento * 1.2);
        }
    }

}

