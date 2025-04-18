package br.com.hackaton.priorizasus.mapper;

import br.com.hackaton.priorizasus.dto.PacienteCadastroPublicoDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.entities.Usuario;
import br.com.hackaton.priorizasus.enums.PermissaoEnum;

public class PacientePublicoMapper {

    public Usuario toUsuario(String login, String senha) {
        return Usuario.builder()
                .login(login)
                .senha(senha)
                .permissao(PermissaoEnum.PACIENTE)
                .build();
    }

    public Paciente toPaciente(PacienteCadastroPublicoDTO dto, Usuario usuario) {
        return Paciente.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .dataNascimento(dto.dataNascimento())
                .telefone(dto.telefone())
                .endereco(dto.endereco())
                .usuario(usuario)
                .build();
    }
}
