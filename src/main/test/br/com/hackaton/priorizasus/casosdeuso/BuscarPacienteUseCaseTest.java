package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.PacienteCadastradoDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class BuscarPacienteUseCaseTest {

    @Mock
    private PacienteRepository pacienteRepository;

    private BuscarPacienteUseCase buscarPacienteUseCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        buscarPacienteUseCase = new BuscarPacienteUseCase(pacienteRepository);
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class BuscarPorId {

        @Test
        void deveRetornarPacienteQuandoIdExistir() {
            Paciente paciente = Paciente.builder()
                    .id(1L)
                    .nome("João")
                    .cpf("123.456.789-00")
                    .build();

            when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

            PacienteCadastradoDTO resultado = buscarPacienteUseCase.buscarPorId(1L);

            assertEquals(1L, resultado.pacienteId());
            assertEquals("João", resultado.nomePaciente());
            assertEquals("123.456.789-00", resultado.cpfPaciente());
        }

        @Test
        void deveLancarExcecaoQuandoPacienteNaoForEncontrado() {
            when(pacienteRepository.findById(2L)).thenReturn(Optional.empty());

            EntidadeNaoEncontradaException exception = assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> buscarPacienteUseCase.buscarPorId(2L)
            );

            assertEquals("Paciente não encontrado", exception.getMessage());
        }
    }
}