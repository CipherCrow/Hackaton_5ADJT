package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.entities.FilaTriagem;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import br.com.hackaton.priorizasus.repository.FilaTriagemRepository;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegistrarChegadaUseCaseTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private FilaTriagemRepository filaTriagemRepository;

    @Mock
    private FilaAtendimentoRepository filaAtendimentoRepository;

    private RegistrarChegadaUseCase registrarChegadaUseCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        registrarChegadaUseCase = new RegistrarChegadaUseCase(
                pacienteRepository,
                filaTriagemRepository,
                filaAtendimentoRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    @DisplayName("Registrar chegada")
    class RegistrarChegada {

        @Test
        @DisplayName("Deve registrar chegada para atendimento administrativo")
        void deveRegistrarChegadaAdministrativo() {
            Paciente paciente = Paciente.builder().id(1L).nome("Maria").build();
            when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

            registrarChegadaUseCase.registrarChegada(1L, true);

            verify(filaAtendimentoRepository, times(1)).save(any(FilaAtendimento.class));
            verify(filaTriagemRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve registrar chegada para triagem")
        void deveRegistrarChegadaTriagem() {
            Paciente paciente = Paciente.builder().id(1L).nome("Maria").build();
            when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

            registrarChegadaUseCase.registrarChegada(1L, false);

            verify(filaTriagemRepository, times(1)).save(any(FilaTriagem.class));
            verify(filaAtendimentoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção se paciente não for encontrado")
        void deveLancarExcecaoPacienteNaoEncontrado() {
            when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    registrarChegadaUseCase.registrarChegada(1L, true));

            verify(filaTriagemRepository, never()).save(any());
            verify(filaAtendimentoRepository, never()).save(any());
        }
    }
}
