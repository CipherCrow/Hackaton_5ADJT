package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.BuscarHistoricoAtendimentosUseCase;
import br.com.hackaton.priorizasus.dto.HistoricoAtendimentoDTO;
import br.com.hackaton.priorizasus.exception.TokenInvalidoException;
import br.com.hackaton.priorizasus.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PacienteControllerTest {

    private MockMvc mockMvc;

    private AutoCloseable openMocks;

    private PacienteController pacienteController;

    @Mock
    private BuscarHistoricoAtendimentosUseCase buscarHistoricoAtendimentosUseCase;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        pacienteController = new PacienteController(buscarHistoricoAtendimentosUseCase, jwtUtil);
        mockMvc = MockMvcBuilders
                .standaloneSetup(pacienteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveRetornarHistoricoDoPacienteComSucesso() throws Exception {
        // Arrange
        String token = "Bearer fake.jwt.token";
        String login = "paciente123";

        List<HistoricoAtendimentoDTO> historico = List.of(
                new HistoricoAtendimentoDTO(
                        "2025-04-10T08:30:00",
                        "Dr. João Silva",
                        "Dor de cabeça, Febre",
                        "Gripe",
                        "Dipirona 500mg",
                        "AMARELO"
                ),
                new HistoricoAtendimentoDTO(
                        "2025-04-11T09:00:00",
                        "Dra. Maria Lima",
                        "Náusea",
                        "Indigestão",
                        "Plasil",
                        "VERDE"
                )
        );

        given(jwtUtil.getUsernameFromToken("fake.jwt.token")).willReturn(login);
        given(buscarHistoricoAtendimentosUseCase.executar(login)).willReturn(historico);

        // Act & Assert
        mockMvc.perform(get("/paciente/historico/buscarTodoHistorico")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].profissional").value("Dr. João Silva"))
                .andExpect(jsonPath("$[1].profissional").value("Dra. Maria Lima"));
    }

    @Test
    void deveLancarExcecaoQuandoTokenForInvalido() throws Exception {
        // Arrange
        String token = "Bearer token.invalido";

        given(jwtUtil.getUsernameFromToken("token.invalido"))
                .willThrow(new TokenInvalidoException("Token inválido."));

        // Act & Assert
        mockMvc.perform(get("/paciente/historico/buscarTodoHistorico")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token inválido."));
    }
}
