package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.AlterarStatusFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarDezProximosFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarPacienteFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.RealizarTriagemUseCase;
import br.com.hackaton.priorizasus.dto.FilaTriagemResponseDTO;
import br.com.hackaton.priorizasus.dto.RealizarTriagemRequestDTO;
import br.com.hackaton.priorizasus.dto.TriagemResponseDTO;
import br.com.hackaton.priorizasus.entities.Sintoma;
import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TriagemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BuscarDezProximosFilaTriagemUseCase buscarProximosAguardando;
    @Mock
    private BuscarPacienteFilaTriagemUseCase buscarPacienteFilaPorCpfUseCase;
    @Mock
    private AlterarStatusFilaTriagemUseCase alterarStatusTriagemUseCase;
    @Mock
    private RealizarTriagemUseCase realizarTriagemUseCase;

    private TriagemController controller;

    private AutoCloseable openMocks;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
         controller = new TriagemController(
                 buscarProximosAguardando,
                 buscarPacienteFilaPorCpfUseCase,
                 alterarStatusTriagemUseCase,
                 realizarTriagemUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(GlobalExceptionHandler.class).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class BuscarProximosAguardandoTests {

        @Test
        void deveRetornarListaDeProximosPacientes() throws Exception {
            List<FilaTriagemResponseDTO> lista = List.of(
                    new FilaTriagemResponseDTO(1L, "Paciente A", "12345678900", StatusTriagemEnum.AGUARDANDO, LocalDateTime.now()),
                    new FilaTriagemResponseDTO(2L, "Paciente B", "98765432100", StatusTriagemEnum.AGUARDANDO, LocalDateTime.now())
            );

            when(buscarProximosAguardando.buscarProximos()).thenReturn(lista);

            mockMvc.perform(get("/triagem/filaTriagem/aguardandoTriagem"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].nomePaciente").value("Paciente A"))
                    .andExpect(jsonPath("$[1].cpfPaciente").value("98765432100"));
        }
    }

    @Nested
    class BuscarPorIdOuCpfTests {

        @Test
        void deveRetornarPacientePorIdOuCpf() throws Exception {
            FilaTriagemResponseDTO dto = new FilaTriagemResponseDTO(1L, "Fulano", "12345678900", StatusTriagemEnum.AGUARDANDO, LocalDateTime.now());

            when(buscarPacienteFilaPorCpfUseCase.buscar("12345678900")).thenReturn(dto);

            mockMvc.perform(get("/triagem/filaTriagem/buscarPorIdOuCpf/12345678900"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nomePaciente").value("Fulano"))
                    .andExpect(jsonPath("$.cpfPaciente").value("12345678900"));
        }

        @Test
        void deveRetornar404QuandoNaoEncontrarPaciente() throws Exception {
            when(buscarPacienteFilaPorCpfUseCase.buscar("99999999999"))
                    .thenThrow(new EntidadeNaoEncontradaException("Paciente não encontrado"));

            mockMvc.perform(get("/triagem/filaTriagem/buscarPorIdOuCpf/99999999999"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Paciente não encontrado"));
        }
    }

    @Nested
    class AlterarStatusTests {

        @Test
        void deveAlterarStatusComSucesso() throws Exception {
            mockMvc.perform(put(
                    "/triagem/filaTriagem/alterarStatus/1")
                            .param("novoStatus", "TRIAGEM_REALIZADA"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Status atualizado com sucesso."));

            verify(alterarStatusTriagemUseCase).alterarStatus(1L, StatusTriagemEnum.TRIAGEM_REALIZADA);
        }

        @Test
        void deveRetornar404QuandoIdNaoExistir() throws Exception {
            doThrow(new EntidadeNaoEncontradaException("Paciente não encontrado"))
                    .when(alterarStatusTriagemUseCase).alterarStatus(99L, StatusTriagemEnum.CANCELADO);

            mockMvc.perform(put("/triagem/filaTriagem/alterarStatus/99")
                            .param("novoStatus", "CANCELADO"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Paciente não encontrado"));
        }

    }

    @Nested
    class IniciarTriagem {

        @Test
        void deveConseguirIniciarTriagem() throws Exception {
            mockMvc.perform(put(
                            "/triagem/filaTriagem/iniciarTriagem/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Triagem iniciada com sucesso."));

            verify(alterarStatusTriagemUseCase).alterarStatus(1L, StatusTriagemEnum.EM_ANDAMENTO);
        }

        @Test
        void deveRetornar404QuandoIdNaoExistir() throws Exception {
            doThrow(new EntidadeNaoEncontradaException("Paciente não encontrado"))
                    .when(alterarStatusTriagemUseCase).alterarStatus(99L, StatusTriagemEnum.EM_ANDAMENTO);

            mockMvc.perform(put("/triagem/filaTriagem/iniciarTriagem/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Paciente não encontrado"));
        }
    }

    @Nested
    class RealizarTriagemEndpointTests {

        @Test
        void deveRealizarTriagemComSucesso() throws Exception {
            Set<Sintoma> sintomas = new HashSet<>();
            sintomas.add(new Sintoma(1L, "Febre", 4));
            sintomas.add(new Sintoma(2L, "Dor de cabeça", 3));

            RealizarTriagemRequestDTO requestDTO = new RealizarTriagemRequestDTO(
                    1L,
                    10L,
                    sintomas,
                    false,
                    null
            );

            TriagemResponseDTO responseDTO = new TriagemResponseDTO(100L, NivelPrioridadeEnum.AMARELO, LocalDateTime.now());
            when(realizarTriagemUseCase.realizarTriagem(any(RealizarTriagemRequestDTO.class)))
                    .thenReturn(responseDTO);

            mockMvc.perform(post("/triagem/realizar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.triagemId").value(100L))
                    .andExpect(jsonPath("$.nivelPrioridadeEnum").value("AMARELO"))
                    .andExpect(jsonPath("$.dataTriagem").exists());
        }

        @Test
        void deveRetornar404QuandoUseCaseLancaExcecao() throws Exception {
            Set<Sintoma> sintomas = new HashSet<>();
            sintomas.add(new Sintoma(1L, "Dor", 2));

            RealizarTriagemRequestDTO requestDTO = new RealizarTriagemRequestDTO(
                    999L,
                    10L,
                    sintomas,
                    false,
                    null
            );

            when(realizarTriagemUseCase.realizarTriagem(any(RealizarTriagemRequestDTO.class)))
                    .thenThrow(new EntidadeNaoEncontradaException("Paciente não encontrado"));

            mockMvc.perform(post("/triagem/realizar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Paciente não encontrado"));
        }
    }


}
