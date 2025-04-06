package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.RegistrarChegadaUseCase;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecepcaoControllerValidacaoTest {

    private MockMvc mockMvc;
    @Mock
    private RegistrarChegadaUseCase registrarChegadaUseCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        RecepcaoController recepcaoController = new RecepcaoController(
                registrarChegadaUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(recepcaoController).setControllerAdvice(GlobalExceptionHandler.class).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class ValidacaoRegistroChegada {

        @Test
        void deveRetornar400QuandoIdPacienteEhNulo() throws Exception {
            String json = """
                {
                    "pacienteId": null,
                    "atendimentoAdministrativo": true
                }
                """;

            mockMvc.perform(post("/recepcao/registrarChegada")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("ID do paciente é obrigatório")));
        }

        @Test
        void deveRetornar400QuandoAtendimentoAdministrativoEhNulo() throws Exception {
            String json = """
                {
                    "pacienteId": 1,
                    "atendimentoAdministrativo": null
                }
                """;

            mockMvc.perform(post("/recepcao/registrarChegada")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("É necessário informar se o atendimento é administrativo")));
        }
    }

}
