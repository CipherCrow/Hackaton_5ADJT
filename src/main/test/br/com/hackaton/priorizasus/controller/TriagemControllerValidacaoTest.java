package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.AlterarStatusFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarDezProximosFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarPacienteFilaTriagemUseCase;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TriagemControllerValidacaoTest {

    private MockMvc mockMvc;

    @Mock
    private BuscarDezProximosFilaTriagemUseCase buscarProximosAguardando;
    @Mock
    private BuscarPacienteFilaTriagemUseCase buscarPacienteFilaPorCpfUseCase;
    @Mock
    private AlterarStatusFilaTriagemUseCase alterarStatusTriagemUseCase;

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
                alterarStatusTriagemUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(GlobalExceptionHandler.class).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class ValidacaoAlterarStatus {

        @Test
        void deveRetornar400QuandoStatusForInvalido() throws Exception {
            mockMvc.perform(put("/triagem/filaTriagem/alterarStatus/1")
                            .param("novoStatus", "INVALIDO"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Valor inválido para o parâmetro: INVALIDO")));
        }

    }
}
