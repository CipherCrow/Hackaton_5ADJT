package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.PacienteCadastroPublicoDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.entities.Usuario;
import br.com.hackaton.priorizasus.enums.PermissaoEnum;
import br.com.hackaton.priorizasus.exception.EntidadeJaExisteException;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import br.com.hackaton.priorizasus.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CadastrarPacientePublicamenteUseCaseTest {

    private CadastrarPacientePublicamenteUseCase useCase;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        useCase = new CadastrarPacientePublicamenteUseCase(usuarioRepository, pacienteRepository, passwordEncoder);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveCadastrarPacienteComSucesso() {
        // Arrange
        PacienteCadastroPublicoDTO dto = new PacienteCadastroPublicoDTO(
                "Maria Silva",
                "12345678900",
                LocalDateTime.of(1995, 7, 20, 0, 0),
                "11999998888",
                "Rua das Palmeiras",
                "maria_login",
                "senha123"
        );

        when(usuarioRepository.findByLogin("maria_login")).thenReturn(Optional.empty());
        when(pacienteRepository.findByCpf("12345678900")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha123")).thenReturn("senhaEncriptada");

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        ArgumentCaptor<Paciente> pacienteCaptor = ArgumentCaptor.forClass(Paciente.class);

        useCase.executar(dto);

        verify(usuarioRepository).save(usuarioCaptor.capture());
        verify(pacienteRepository).save(pacienteCaptor.capture());

        Usuario usuarioSalvo = usuarioCaptor.getValue();
        assertEquals("maria_login", usuarioSalvo.getLogin());
        assertEquals("senhaEncriptada", usuarioSalvo.getSenha());
        assertEquals(PermissaoEnum.PACIENTE, usuarioSalvo.getPermissao());

        Paciente pacienteSalvo = pacienteCaptor.getValue();
        assertEquals("Maria Silva", pacienteSalvo.getNome());
        assertEquals("12345678900", pacienteSalvo.getCpf());
        assertEquals("11999998888", pacienteSalvo.getTelefone());
        assertEquals("Rua das Palmeiras", pacienteSalvo.getEndereco());
        assertEquals(usuarioSalvo, pacienteSalvo.getUsuario());
    }

    @Test
    void deveLancarExcecaoQuandoLoginJaExiste() {
        PacienteCadastroPublicoDTO dto = new PacienteCadastroPublicoDTO(
                "Carlos",
                "99999999999",
                LocalDateTime.of(1990, 1, 1, 0, 0),
                "11999999999",
                "Rua XPTO",
                "login_existente",
                "senha"
        );

        when(usuarioRepository.findByLogin("login_existente"))
                .thenReturn(Optional.of(mock(Usuario.class)));

        EntidadeJaExisteException ex = assertThrows(EntidadeJaExisteException.class,
                () -> useCase.executar(dto));

        assertEquals("Já existe um usuário com esse login.", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste() {
        PacienteCadastroPublicoDTO dto = new PacienteCadastroPublicoDTO(
                "Joana",
                "11122233344",
                LocalDateTime.of(1998, 4, 12, 0, 0),
                "11987654321",
                "Rua Nova",
                "joana_login",
                "senha123"
        );

        when(usuarioRepository.findByLogin("joana_login")).thenReturn(Optional.empty());
        when(pacienteRepository.findByCpf("11122233344"))
                .thenReturn(Optional.of(mock(Paciente.class)));

        EntidadeJaExisteException ex = assertThrows(EntidadeJaExisteException.class,
                () -> useCase.executar(dto));

        assertEquals("Já existe um paciente com esse CPF.", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
        verify(pacienteRepository, never()).save(any());
    }
}

