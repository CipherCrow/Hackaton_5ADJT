package br.com.hackaton.priorizasus.entities;

import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import jakarta.persistence.*;
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
public class FilaTriagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Paciente paciente;

    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusTriagemEnum statusTriagem;

    @PastOrPresent
    private LocalDateTime horarioEntrada;
}