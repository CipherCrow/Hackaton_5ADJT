package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.PacienteCadastroPublicoDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.entities.Usuario;
import br.com.hackaton.priorizasus.enums.PermissaoEnum;
import br.com.hackaton.priorizasus.exception.EntidadeJaExisteException;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import br.com.hackaton.priorizasus.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarPacientePublicamenteUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void executar(PacienteCadastroPublicoDTO dto) {
        // Validação de duplicidade

        if (!usuarioRepository.findByLogin(dto.login()).isEmpty()) {
            throw new EntidadeJaExisteException("Já existe um usuário com esse login.");
        }

        if (!pacienteRepository.findByCpf(dto.cpf()).isEmpty()) {
            throw new EntidadeJaExisteException("Já existe um paciente com esse CPF.");
        }

        Usuario usuario = Usuario.builder()
                .login(dto.login())
                .senha(passwordEncoder.encode(dto.senha()))
                .permissao(PermissaoEnum.PACIENTE)
                .build();

        usuarioRepository.save(usuario);

        Paciente paciente = Paciente.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .dataNascimento(dto.dataNascimento())
                .telefone(dto.telefone())
                .endereco(dto.endereco())
                .usuario(usuario)
                .build();

        pacienteRepository.save(paciente);
    }
}


