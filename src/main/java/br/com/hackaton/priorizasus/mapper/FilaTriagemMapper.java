package br.com.hackaton.priorizasus.mapper;

import br.com.hackaton.priorizasus.dto.FilaTriagemResponseDTO;
import br.com.hackaton.priorizasus.entities.FilaTriagem;

public class FilaTriagemMapper {

    private FilaTriagemMapper(){
        throw new IllegalStateException("Classe de Utilidade!");
    }

    public static FilaTriagemResponseDTO mapearParaReponseDTO(FilaTriagem fila) {

        return new FilaTriagemResponseDTO(
                fila.getId(),
                fila.getPaciente().getNome(),
                fila.getPaciente().getCpf(),
                fila.getStatusTriagem(),
                fila.getHorarioEntrada()
        );

    }
}
