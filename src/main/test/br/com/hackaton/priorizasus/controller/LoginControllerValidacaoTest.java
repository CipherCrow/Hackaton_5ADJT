package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.dto.LoginRequestDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerValidacaoTest {

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
    class ValidacaoCamposDTO {

        @Test
        void deveRetornarBadRequestQuandoUsuarioVazio() throws Exception {
            LoginRequestDTO dto = new LoginRequestDTO("", "senha123");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.usuario").value("Deve ser inserido um usuario!"));
        }

        @Test
        void deveRetornarBadRequestQuandoSenhaVazia() throws Exception {
            LoginRequestDTO dto = new LoginRequestDTO("usuario123", "");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.senha").value("Deve ser inserido uma senha!"));
        }
    }

}

