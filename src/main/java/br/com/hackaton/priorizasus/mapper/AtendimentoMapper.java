package br.com.hackaton.priorizasus.mapper;

import br.com.hackaton.priorizasus.dto.FilaAtendimentoDTO;
import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;

public class AtendimentoMapper {

    private AtendimentoMapper(){
        throw new IllegalStateException("Classe de Utilidade!");
    }

    public static FilaAtendimentoDTO toDTO(FilaAtendimento atendimento) {

        // Todo: pendente ajustar sem triagem
        if(atendimento.getTriagem() == null){
            return new FilaAtendimentoDTO(
                    atendimento.getId(),
                    "Paciente sem triagem",
                    NivelPrioridadeEnum.AZUL,
                    atendimento.getHorarioEntradaFila(),
                    atendimento.getTempoEsperaEstimado(),
                    atendimento.getStatusAtendimentoEnum()
            );
        }

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
