package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.CriarUsuarioPacienteDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.entities.Usuario;
import br.com.hackaton.priorizasus.enums.PermissaoEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import br.com.hackaton.priorizasus.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarUsuarioPacienteUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void executar(CriarUsuarioPacienteDTO dto) {
        Paciente paciente = pacienteRepository.findByCpf(dto.cpf())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Paciente com CPF informado não encontrado"));

        if (paciente.getUsuario() != null) {
            throw new IllegalArgumentException("Este paciente já possui um usuário vinculado.");
        }

        if (!usuarioRepository.findByLogin(dto.login()).isEmpty()) {
            throw new IllegalArgumentException("Login já está em uso");
        }

        Usuario novoUsuario = Usuario.builder()
                .login(dto.login())
                .senha(passwordEncoder.encode(dto.senha()))
                .permissao(PermissaoEnum.PACIENTE)
                .build();

        paciente.setUsuario(novoUsuario);
        pacienteRepository.save(paciente);
    }
}

