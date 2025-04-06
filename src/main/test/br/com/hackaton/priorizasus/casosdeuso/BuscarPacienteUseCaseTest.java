package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.PacienteCadastradoDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BuscarPacienteUseCaseTest {

    private final PacienteRepository pacienteRepository = mock(PacienteRepository.class);
    private final BuscarPacienteUseCase buscarPacienteUseCase = new BuscarPacienteUseCase(pacienteRepository);

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