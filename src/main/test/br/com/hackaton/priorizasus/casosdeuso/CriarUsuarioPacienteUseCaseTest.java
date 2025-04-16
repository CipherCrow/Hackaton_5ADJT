package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.CriarUsuarioPacienteDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.entities.Usuario;
import br.com.hackaton.priorizasus.enums.PermissaoEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import br.com.hackaton.priorizasus.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CriarUsuarioPacienteUseCaseTest {
    private AutoCloseable openMocks;

    private CriarUsuarioPacienteUseCase useCase;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CriarUsuarioPacienteDTO dto;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        useCase = new CriarUsuarioPacienteUseCase(usuarioRepository, pacienteRepository, passwordEncoder);

        dto = new CriarUsuarioPacienteDTO("joao","senha123","12345678");
        paciente = Paciente.builder()
                .id(1L)
                .cpf("12345678")
                .nome("João da Silva")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class QuandoExecutarComSucesso {
        @Test
        void deveCriarUsuarioEVincularAoPaciente() {
            when(pacienteRepository.findByCpf(dto.cpf())).thenReturn(Optional.of(paciente));
            when(usuarioRepository.findByLogin(dto.login())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(dto.senha())).thenReturn("senhaCriptografada");

            useCase.executar(dto);

            assertNotNull(paciente.getUsuario());
            assertEquals("joao", paciente.getUsuario().getLogin());
            assertEquals("senhaCriptografada", paciente.getUsuario().getSenha());
            assertEquals(PermissaoEnum.PACIENTE, paciente.getUsuario().getPermissao());

            verify(pacienteRepository).save(paciente);
        }
    }

    @Nested
    class QuandoPacienteNaoExiste {
        @Test
        void deveLancarExcecaoEntidadeNaoEncontrada() {
            when(pacienteRepository.findByCpf(dto.cpf())).thenReturn(Optional.empty());

            assertThrows(EntidadeNaoEncontradaException.class, () -> useCase.executar(dto));
        }
    }

    @Nested
    class QuandoPacienteJaTemUsuario {
        @Test
        void deveLancarExcecaoPacienteJaPossuiUsuario() {
            paciente.setUsuario(Usuario.builder().id(2L).login("existente").build());
            when(pacienteRepository.findByCpf(dto.cpf())).thenReturn(Optional.of(paciente));

            assertThrows(IllegalArgumentException.class, () -> useCase.executar(dto));
        }
    }

    @Nested
    class QuandoLoginJaEstaEmUso {
        @Test
        void deveLancarExcecaoLoginJaEmUso() {
            when(pacienteRepository.findByCpf(dto.cpf())).thenReturn(Optional.of(paciente));
            when(usuarioRepository.findByLogin(dto.login()))
                    .thenReturn(Optional.of(Usuario.builder().id(99L).login("joao").build()));

            assertThrows(IllegalArgumentException.class, () -> useCase.executar(dto));
        }
    }
}


