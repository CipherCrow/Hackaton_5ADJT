package br.com.hackaton.priorizasus.mapper;

import br.com.hackaton.priorizasus.dto.PacienteCadastradoDTO;
import br.com.hackaton.priorizasus.dto.PacienteParaCadastrarDTO;
import br.com.hackaton.priorizasus.entities.Paciente;

public class PacienteMapper {

    private PacienteMapper(){
        throw new IllegalStateException("Classe de Utilidade!");
    }

    public static Paciente toEntity(PacienteParaCadastrarDTO dto) {
        return Paciente.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .dataNascimento(dto.dataNascimento())
                .telefone(dto.telefone())
                .endereco(dto.endereco())
                .build();
    }

    public static PacienteCadastradoDTO toDTO(Paciente paciente) {
        return new PacienteCadastradoDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf()
        );
    }
}
