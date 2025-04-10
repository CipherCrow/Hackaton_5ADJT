package br.com.hackaton.priorizasus.mapper;

import br.com.hackaton.priorizasus.dto.ProfissionalSaudeDTO;
import br.com.hackaton.priorizasus.dto.ProfissionalSaudeRequestDTO;
import br.com.hackaton.priorizasus.entities.ProfissionalSaude;

public class ProfissionalSaudeMapper {

    private ProfissionalSaudeMapper(){
        throw new IllegalStateException("Classe de Utilidade!");
    }


    public static ProfissionalSaude toEntity(ProfissionalSaudeRequestDTO dto) {
        return new ProfissionalSaude(
                null,
                dto.nome(),
                dto.crm(),
                dto.especialidade(),
                null
        );
    }

    public static ProfissionalSaudeDTO toDTO(ProfissionalSaude profissional) {
        return new ProfissionalSaudeDTO(
                profissional.getId(),
                profissional.getNome(),
                profissional.getCrm(),
                profissional.getEspecialidade()
        );
    }
}
