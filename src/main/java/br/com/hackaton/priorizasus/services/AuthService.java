package br.com.hackaton.priorizasus.services;

import br.com.hackaton.priorizasus.dto.LoginRequestDTO;
import br.com.hackaton.priorizasus.entities.Usuario;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.infrastructure.security.JwtUtil;
import br.com.hackaton.priorizasus.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public String autenticar(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLogin(dto.usuario())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário ou senha inválidos"));

        if (!usuario.getSenha().equals(dto.senha())) {
            throw new IllegalArgumentException("Usuário ou senha inválidos");
        }

        return jwtUtil.gerarToken(usuario.getLogin(), usuario.getPermissao());
    }
}

