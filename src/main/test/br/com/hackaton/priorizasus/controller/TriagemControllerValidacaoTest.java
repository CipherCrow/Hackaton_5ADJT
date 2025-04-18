package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.AlterarStatusFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarDezProximosFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarPacienteFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.RealizarTriagemUseCase;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    @Mock
    private RealizarTriagemUseCase realizarTriagemUseCase;

    private TriagemController controller;

    private AutoCloseable openMocks;

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
    class ValidacaoAlterarStatus {

        @Test
        void deveRetornar400QuandoStatusForInvalido() throws Exception {
            mockMvc.perform(put("/triagem/filaTriagem/alterarStatus/1")
                            .param("novoStatus", "INVALIDO"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Valor inválido para o parâmetro: INVALIDO")));
        }

    }

    @Test
    void ValidacaoRealizarTraigemdeveRetornar400QuandoDTOForInvalido() throws Exception {
        // Falta pacienteId e sintomas estão vazios
        String json = """
                    {
                        "pacienteId": null,
                        "profissionalId": 10,
                        "sintomas": [],
                        "prioridadeManual": false
                    }
                    """;

        mockMvc.perform(post("/triagem/realizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Paciente ID é obrigatório")))
                .andExpect(content().string(containsString("Deve haver pelo menos um sintoma")));
    }
}
