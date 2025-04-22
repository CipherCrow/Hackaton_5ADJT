package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.AtualizarStatusAtendimentoUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarFilaAtendimentoPorIdOuCpfUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarProximosAtendimentosUseCase;
import br.com.hackaton.priorizasus.casosdeuso.FinalizarAtendimentoUseCase;
import br.com.hackaton.priorizasus.dto.FilaAtendimentoDTO;
import br.com.hackaton.priorizasus.dto.FinalizarAtendimentoDTO;
import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AtendimentoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BuscarProximosAtendimentosUseCase buscarProximosUseCase;

    @Mock
    private BuscarFilaAtendimentoPorIdOuCpfUseCase buscarPorIdOuCpfUseCase;

    @Mock
    private AtualizarStatusAtendimentoUseCase atualizarStatusUseCase;

    @Mock
    private FinalizarAtendimentoUseCase finalizarAtendimentoUseCase;

    private AutoCloseable openMocks;

    private AtendimentoController controller;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        controller = new AtendimentoController(
                buscarProximosUseCase   , buscarPorIdOuCpfUseCase, atualizarStatusUseCase, finalizarAtendimentoUseCase
                );
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(GlobalExceptionHandler.class).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class BuscarProximosAtendimentos {

        @Test
        void deveRetornarListaComSucesso() throws Exception {
            FilaAtendimentoDTO dto = new FilaAtendimentoDTO(
                    1L,
                    "João",
                    NivelPrioridadeEnum.VERMELHO,
                    LocalDateTime.now(),
                    LocalTime.of(0, 5),
                    StatusAtendimentoEnum.PENDENTE
            );

            List<FilaAtendimentoDTO> lista = List.of(dto);
            when(buscarProximosUseCase.executar()).thenReturn(lista);

            mockMvc.perform(get("/filaAtendimento/aguardando"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].nomePaciente").value("João"))
                    .andExpect(jsonPath("$[0].nivelPrioridade").value("VERMELHO"))
                    .andExpect(jsonPath("$[0].status").value("PENDENTE"));
        }
    }

    @Nested
    class BuscarPorIdOuCpf {

        @Test
        void deveRetornarDTOComSucesso() throws Exception {
            FilaAtendimentoDTO dto = new FilaAtendimentoDTO(
                    1L,
                    "Maria",
                    NivelPrioridadeEnum.AMARELO,
                    LocalDateTime.now(),
                    LocalTime.of(0, 10),
                    StatusAtendimentoEnum.EM_ATENDIMENTO
            );

            when(buscarPorIdOuCpfUseCase.executar("12345678900")).thenReturn(dto);

            mockMvc.perform(get("/filaAtendimento/buscarPorIdOuCpf/12345678900"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nomePaciente").value("Maria"))
                    .andExpect(jsonPath("$.nivelPrioridade").value("AMARELO"))
                    .andExpect(jsonPath("$.status").value("EM_ATENDIMENTO"));
        }

        @Test
        void deveRetornarNotFoundQuandoNaoEncontrar() throws Exception {
            when(buscarPorIdOuCpfUseCase.executar("123"))
                    .thenThrow(new EntidadeNaoEncontradaException("Não encontrado"));

            mockMvc.perform(get("/filaAtendimento/buscarPorIdOuCpf/123"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Não encontrado"));
        }
    }

    @Nested
    class AtualizarStatus {

        @Test
        void deveAtualizarComSucesso() throws Exception {
            mockMvc.perform(put("/filaAtendimento/alterarStatus/1")
                            .param("novoStatus", "EM_ATENDIMENTO"))
                    .andExpect(status().isNoContent());

            verify(atualizarStatusUseCase).executar(1L, StatusAtendimentoEnum.EM_ATENDIMENTO);
        }

        @Test
        void deveRetornarBadRequestParaStatusInvalido() throws Exception {
            mockMvc.perform(put("/filaAtendimento/alterarStatus/1")
                            .param("novoStatus", "INVALIDO"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Valor inválido para o parâmetro: INVALIDO"));
        }
    }

    @Nested
    class IniciarAtendimento {

        @Test
        void deveIniciarAtendimentoComSucesso() throws Exception {
            mockMvc.perform(put("/filaAtendimento/iniciarAtendimento/1")
                            .param("novoStatus", "EM_ATENDIMENTO"))
                    .andExpect(status().isOk());

            verify(atualizarStatusUseCase).executar(1L, StatusAtendimentoEnum.EM_ATENDIMENTO);
        }
    }

    @Nested
    class finalizarAtendimento{
        @Test
        void deveRetornar200QuandoFinalizadoComSucesso() throws Exception {
            FinalizarAtendimentoDTO dto = new FinalizarAtendimentoDTO(
                    1L,
                    2L,
                    "Diagnóstico exemplo",
                    "Prescrição exemplo"
            );

            mockMvc.perform(put("/filaAtendimento/finalizar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Atendimento Finalizado!"));

            verify(finalizarAtendimentoUseCase).executar(dto);
        }

        @Test
        void deveRetornar404QuandoAtendimentoNaoForEncontrado() throws Exception {
            FinalizarAtendimentoDTO dto = new FinalizarAtendimentoDTO(
                    99L,
                    2L,
                    "Diagnóstico",
                    "Prescrição"
            );

            doThrow(new EntidadeNaoEncontradaException("Atendimento não encontrado"))
                    .when(finalizarAtendimentoUseCase).executar(dto);

            mockMvc.perform(put("/filaAtendimento/finalizar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Atendimento não encontrado"));
        }

        @Test
        void deveRetornar400QuandoStatusDoAtendimentoNaoPermiteFinalizacao() throws Exception {
            FinalizarAtendimentoDTO dto = new FinalizarAtendimentoDTO(
                    1L,
                    2L,
                    "Diagnóstico",
                    "Prescrição"
            );

            doThrow(new IllegalArgumentException("Apenas atendimentos pendentes podem ser finalizados"))
                    .when(finalizarAtendimentoUseCase).executar(dto);

            mockMvc.perform(put("/filaAtendimento/finalizar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Apenas atendimentos pendentes podem ser finalizados"));
        }
    }
}
