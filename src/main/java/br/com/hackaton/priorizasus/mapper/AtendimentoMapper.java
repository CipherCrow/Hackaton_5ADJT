package br.com.hackaton.priorizasus.mapper;

import br.com.hackaton.priorizasus.dto.FilaAtendimentoDTO;
import br.com.hackaton.priorizasus.entities.FilaAtendimento;

public class AtendimentoMapper {

    private AtendimentoMapper(){
        throw new IllegalStateException("Classe de Utilidade!");
    }

    public static FilaAtendimentoDTO toDTO(FilaAtendimento atendimento) {
        return new FilaAtendimentoDTO(
                atendimento.getId(),
                atendimento.getTriagem().getPaciente().getNome(),
                atendimento.getTriagem().getNivelPrioridadeEnum(),
                atendimento.getHorarioEntradaFila(),
                atendimento.getTempoEsperaEstimado(),
                atendimento.getStatusAtendimentoEnum()
        );

    }

}
