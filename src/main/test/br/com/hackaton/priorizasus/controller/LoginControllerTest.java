package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.dto.LoginRequestDTO;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    private LoginController loginController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        loginController = new LoginController(authService);

        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class AutenticacaoValida {

        @Test
        void deveAutenticarUsuarioERetornarToken() throws Exception {
            LoginRequestDTO dto = new LoginRequestDTO("usuarioTeste", "senhaSegura");
            String tokenEsperado = "fake.jwt.token";

            when(authService.autenticar(dto)).thenReturn(tokenEsperado);

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value(tokenEsperado));
        }
    }

    @Nested
    class FalhasDeAutenticacao {

        @Test
        void deveRetornarNotFoundQuandoUsuarioNaoExiste() throws Exception {
            LoginRequestDTO dto = new LoginRequestDTO("naoExiste", "senha123");

            when(authService.autenticar(dto))
                    .thenThrow(new EntidadeNaoEncontradaException("Usuário ou senha inválidos"));

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Usuário ou senha inválidos"));
        }

        @Test
        void deveRetornarBadRequestQuandoSenhaIncorreta() throws Exception {
            LoginRequestDTO dto = new LoginRequestDTO("usuario", "senhaErrada");

            when(authService.autenticar(dto))
                    .thenThrow(new IllegalArgumentException("Usuário ou senha inválidos"));

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Usuário ou senha inválidos"));
        }
    }
}

