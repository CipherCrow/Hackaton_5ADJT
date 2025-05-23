package br.com.hackaton.priorizasus.mapper;

import br.com.hackaton.priorizasus.dto.HistoricoAtendimentoDTO;
import br.com.hackaton.priorizasus.entities.AtendimentoHistorico;
import br.com.hackaton.priorizasus.entities.Sintoma;
import br.com.hackaton.priorizasus.entities.Triagem;

import java.util.stream.Collectors;

public class HistoricoAtendimentoMapper {

    private HistoricoAtendimentoMapper() {
        throw new IllegalStateException("Classe de utilidade!");
    }

    public static HistoricoAtendimentoDTO toDTO(AtendimentoHistorico atendimento) {
        Triagem triagem = atendimento.getTriagem();

        return new HistoricoAtendimentoDTO(
                atendimento.getDataAtendimento().toString(),
                atendimento.getProfissional().getNome(),
                triagem.getSintomas().stream()
                        .map(Sintoma::getDescricao)
                        .collect(Collectors.joining(", ")),
                atendimento.getDiagnostico(),
                atendimento.getPrescricao(),
                triagem.getNivelPrioridadeEnum().name()
        );
    }
}
