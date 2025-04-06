package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.PacienteParaCadastrarDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AtualizarPacienteUseCaseTest {

    private AtualizarPacienteUseCase atualizarPacienteUseCase;

    @Mock
    private PacienteRepository pacienteRepository;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        atualizarPacienteUseCase = new AtualizarPacienteUseCase(pacienteRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class Atualizar {

        @Test
        void deveAtualizarPacienteComSucesso() {
            Paciente pacienteExistente = Paciente.builder()
                    .id(1L)
                    .nome("Antigo")
                    .telefone("0000")
                    .endereco("Antigo")
                    .dataNascimento(LocalDateTime.of(1990, 1, 1, 0, 0))
                    .build();

            PacienteParaCadastrarDTO dto = new PacienteParaCadastrarDTO(
                    "Novo",
                    "12345678900",
                    LocalDateTime.of(1985, 5, 15, 0, 0),
                    "9999",
                    "Novo Endereço"
            );

            when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExistente));

            atualizarPacienteUseCase.atualizar(1L, dto);

            verify(pacienteRepository).save(pacienteExistente);
            assertEquals("Novo", pacienteExistente.getNome());
            assertEquals("9999", pacienteExistente.getTelefone());
            assertEquals("Novo Endereço", pacienteExistente.getEndereco());
            assertEquals(LocalDateTime.of(1985, 5, 15, 0, 0), pacienteExistente.getDataNascimento());
        }

        @Test
        void deveLancarExcecaoQuandoPacienteNaoForEncontrado() {
            when(pacienteRepository.findById(2L)).thenReturn(Optional.empty());

            EntidadeNaoEncontradaException exception = assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> atualizarPacienteUseCase.atualizar(2L, mock(PacienteParaCadastrarDTO.class))
            );

            assertEquals("Paciente não encontrado", exception.getMessage());
        }
    }
}
