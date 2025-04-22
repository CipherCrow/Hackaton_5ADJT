package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.RealizarTriagemRequestDTO;
import br.com.hackaton.priorizasus.dto.SintomaDTO;
import br.com.hackaton.priorizasus.dto.TriagemResponseDTO;
import br.com.hackaton.priorizasus.entities.*;
import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RealizarTriagemUseCaseTest {

    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private ProfissionalSaudeRepository profissionalRepository;
    @Mock
    private TriagemRepository triagemRepository;
    @Mock
    private FilaAtendimentoRepository filaAtendimentoRepository;
    @Mock
    private FilaTriagemRepository filaTriagemRepository;
    @Mock
    private SintomaRepository sintomaRepository;
    @Mock
    private CalcularTempoEstimadoUseCase calcularTempoEstimadoUseCase;
    @Mock
    private  VisualizarSintomaPorIdUseCase visualizarSintomaPorIdUseCase;


    private RealizarTriagemUseCase realizarTriagemUseCase;

    private AutoCloseable openMocks;

    private Paciente paciente;
    private ProfissionalSaude profissional;
    private Triagem triagem;
    private FilaTriagem filaTriagem;
    private FilaAtendimento filaAtendimento;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        visualizarSintomaPorIdUseCase = new VisualizarSintomaPorIdUseCase(sintomaRepository);

        realizarTriagemUseCase = new RealizarTriagemUseCase(
                pacienteRepository,
                profissionalRepository,
                triagemRepository,
                filaAtendimentoRepository,
                filaTriagemRepository,
                calcularTempoEstimadoUseCase,
                visualizarSintomaPorIdUseCase
        );

        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("João");
        paciente.setCpf("11122233344");

        profissional = new ProfissionalSaude();
        profissional.setId(10L);
        profissional.setNome("Dra. Ana");

        triagem = new Triagem();
        triagem.setId(100L);
        triagem.setPaciente(paciente);
        triagem.setProfissional(profissional);


        triagem.setNivelPrioridadeEnum(NivelPrioridadeEnum.AMARELO);
        triagem.setPrioridadeManual(false);
        triagem.setDataTriagem(LocalDateTime.now());

        filaTriagem = new FilaTriagem();
        filaTriagem.setId(200L);
        filaTriagem.setPaciente(paciente);
        filaTriagem.setStatusTriagem(StatusTriagemEnum.AGUARDANDO);
        filaTriagem.setHorarioEntrada(LocalDateTime.now().minusMinutes(5));

        filaAtendimento = new FilaAtendimento();
        filaAtendimento.setId(300L);
        filaAtendimento.setTriagem(triagem);
        filaAtendimento.setAtendimentoAdministrativo(false);
        filaAtendimento.setHorarioEntradaFila(LocalDateTime.now());
        filaAtendimento.setStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE);
        filaAtendimento.setPesoFila(0);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class FluxoSucesso {

        @Test
        void deveRealizarTriagemComSucesso() {
            // Preparação do DTO de requisição
            RealizarTriagemRequestDTO requestDTO = new RealizarTriagemRequestDTO(
                    1L,
                    10L,
                    Set.of(
                            new Sintoma(1L, "Dor de cabeça", 3),
                            new Sintoma(2L, "Febre", 4)
                    ),
        // Supondo  que o antendente optou por deixar fazer automaticamente
                    false,
    null
            );


            when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
            when(profissionalRepository.findById(10L)).thenReturn(Optional.of(profissional));
            when(triagemRepository.save(any(Triagem.class))).thenReturn(triagem);
            when(filaAtendimentoRepository.save(any(FilaAtendimento.class))).thenReturn(filaAtendimento);
            when(filaTriagemRepository.findByPacienteIdAndStatusTriagem(1L,StatusTriagemEnum.EM_ANDAMENTO)).thenReturn(Optional.of(filaTriagem));
            when(filaTriagemRepository.save(any(FilaTriagem.class))).thenReturn(filaTriagem);
            when(sintomaRepository.findById(1L)).thenReturn(Optional.of(new Sintoma(1L, "Dor de cabeça", 3)));
            when(sintomaRepository.findById(2L)).thenReturn(Optional.of(new Sintoma(2L, "Febre", 4)));
            TriagemResponseDTO responseDTO = realizarTriagemUseCase.realizarTriagem(requestDTO);

            assertNotNull(responseDTO);
            assertEquals(100L, responseDTO.triagemId());
            assertEquals(NivelPrioridadeEnum.AMARELO, responseDTO.nivelPrioridadeEnum());
            assertNotNull(responseDTO.dataTriagem());

            verify(pacienteRepository).findById(1L);
            verify(profissionalRepository).findById(10L);
            verify(triagemRepository).save(any(Triagem.class));
            verify(filaAtendimentoRepository).save(any(FilaAtendimento.class));
            verify(filaTriagemRepository).findByPacienteIdAndStatusTriagem(1L,StatusTriagemEnum.EM_ANDAMENTO);
            verify(filaTriagemRepository).save(any(FilaTriagem.class));
        }
    }

    @Nested
    class FluxoExcecoes {

        @Test
        void deveLancarExcecaoQuandoPacienteNaoExistir() {
            RealizarTriagemRequestDTO requestDTO = new RealizarTriagemRequestDTO(
                    999L,
                    10L,
                    Set.of(new Sintoma(1L, "Dor", 2)),
                    false,
                    null
            );

            when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

            EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                    () -> realizarTriagemUseCase.realizarTriagem(requestDTO));

            assertEquals("Paciente não encontrado", ex.getMessage());
            verify(pacienteRepository).findById(999L);
            verify(profissionalRepository, never()).findById(anyLong());
        }

        @Test
        void deveLancarExcecaoQuandoProfissionalNaoExistir() {
            RealizarTriagemRequestDTO requestDTO = new RealizarTriagemRequestDTO(
                    1L,
                    999L,
                    Set.of(new Sintoma(1L, "Dor", 2)),
                    false,
                    null
            );

            when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
            when(profissionalRepository.findById(999L)).thenReturn(Optional.empty());

            EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                    () -> realizarTriagemUseCase.realizarTriagem(requestDTO));

            assertEquals("Profissional não encontrado", ex.getMessage());
            verify(pacienteRepository).findById(1L);
            verify(profissionalRepository).findById(999L);
        }

        @Test
        void deveLancarExcecaoQuandoFilaTriagemNaoForEncontrada() {
            RealizarTriagemRequestDTO requestDTO = new RealizarTriagemRequestDTO(
                    1L,
                    10L,
                    Set.of(new Sintoma(1L, "Dor", 2)),
                    false,
                    null

            );

            when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
            when(profissionalRepository.findById(10L)).thenReturn(Optional.of(profissional));
            // Simula que o paciente não está na fila de triagem
            when(filaTriagemRepository.findByPacienteIdAndStatusTriagem(1L,StatusTriagemEnum.EM_ANDAMENTO)).thenReturn(Optional.empty());
            when(sintomaRepository.findById(1L)).thenReturn(Optional.of(new Sintoma(1L, "Dor", 2)));
            EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                    () -> realizarTriagemUseCase.realizarTriagem(requestDTO));

            assertEquals("Paciente não encontrado na fila de triagem", ex.getMessage());
            verify(filaTriagemRepository).findByPacienteIdAndStatusTriagem(1L,StatusTriagemEnum.EM_ANDAMENTO);
        }
    }
}