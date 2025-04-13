package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.AtualizarStatusAtendimentoUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarFilaAtendimentoPorIdOuCpfUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarProximosAtendimentosUseCase;
import br.com.hackaton.priorizasus.casosdeuso.FinalizarAtendimentoUseCase;
import br.com.hackaton.priorizasus.dto.FinalizarAtendimentoDTO;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AtendimentoControllerValidacaoTest {

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
    class QuandoFinalizaAtendimento {

        @Test
        void deveRetornar400QuandoCampoObrigatorioEstiverFaltando() throws Exception {
            // Diagnóstico está em branco
            FinalizarAtendimentoDTO dto = new FinalizarAtendimentoDTO(
                    1L,
                    2L,
                    "   ",
                    "Prescrição exemplo"
            );

            mockMvc.perform(put("/filaAtendimento/finalizar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.diagnostico").value("O Diagnostico é obrigatório!"));
            verify(finalizarAtendimentoUseCase, never()).executar(any());
        }
    }
}
