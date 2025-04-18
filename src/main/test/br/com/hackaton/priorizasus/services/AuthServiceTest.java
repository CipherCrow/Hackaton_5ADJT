package br.com.hackaton.priorizasus.services;

import br.com.hackaton.priorizasus.dto.LoginRequestDTO;
import br.com.hackaton.priorizasus.entities.Usuario;
import br.com.hackaton.priorizasus.enums.PermissaoEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.infrastructure.security.JwtUtil;
import br.com.hackaton.priorizasus.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AutoCloseable openMocks;

    private AuthService authService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        authService = new AuthService(usuarioRepository, jwtUtil, passwordEncoder);
        usuario = Usuario.builder()
                .login("usuarioTest")
                .senha("senhaCodificada")
                .permissao(PermissaoEnum.PACIENTE)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void autenticar_DeveRetornarToken_QuandoCredenciaisForemValidas() {
        LoginRequestDTO dto = new LoginRequestDTO("usuarioTest", "senha123");

        when(usuarioRepository.findByLogin("usuarioTest")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha123", "senhaCodificada")).thenReturn(true);
        when(jwtUtil.gerarToken("usuarioTest", PermissaoEnum.PACIENTE)).thenReturn("token123");

        String token = authService.autenticar(dto);

        assertEquals("token123", token);
        verify(usuarioRepository).findByLogin("usuarioTest");
        verify(passwordEncoder).matches("senha123", "senhaCodificada");
        verify(jwtUtil).gerarToken("usuarioTest", PermissaoEnum.PACIENTE);
    }

    @Test
    void autenticar_DeveLancarEntidadeNaoEncontradaException_QuandoUsuarioNaoExistir() {
        LoginRequestDTO dto = new LoginRequestDTO("naoexiste", "senha123");

        when(usuarioRepository.findByLogin("naoexiste")).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> authService.autenticar(dto));
        verify(usuarioRepository).findByLogin("naoexiste");
        verifyNoInteractions(passwordEncoder, jwtUtil);
    }

    @Test
    void autenticar_DeveLancarIllegalArgumentException_QuandoSenhaForIncorreta() {
        LoginRequestDTO dto = new LoginRequestDTO("usuarioTest", "senhaErrada");

        when(usuarioRepository.findByLogin("usuarioTest")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaErrada", "senhaCodificada")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.autenticar(dto));
        verify(usuarioRepository).findByLogin("usuarioTest");
        verify(passwordEncoder).matches("senhaErrada", "senhaCodificada");
        verifyNoInteractions(jwtUtil);
    }
}

