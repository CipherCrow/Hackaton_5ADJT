package br.com.hackaton.priorizasus.mapper;

import br.com.hackaton.priorizasus.dto.SintomaDTO;
import br.com.hackaton.priorizasus.dto.SintomaRequestDTO;
import br.com.hackaton.priorizasus.entities.Sintoma;

public class SintomaMapper {

    private SintomaMapper(){
        throw new IllegalStateException("Classe de Utilidade!");
    }

    public static Sintoma toEntity(SintomaRequestDTO dto) {
        return Sintoma.builder()
                .id(null)
                .descricao(dto.descricao())
                .gravidade(dto.gravidade())
                .build();
    }

    public static SintomaDTO toDTO(Sintoma sintoma) {
        return new SintomaDTO(
                sintoma.getId(),
                sintoma.getDescricao(),
                sintoma.getGravidade()
        );
    }
}
