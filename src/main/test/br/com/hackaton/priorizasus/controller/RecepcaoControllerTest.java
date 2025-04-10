package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.RegistrarChegadaUseCase;
import br.com.hackaton.priorizasus.dto.RegistroChegadaDTO;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecepcaoControllerTest {

    private MockMvc mockMvc;
    @Mock
    private RegistrarChegadaUseCase registrarChegadaUseCase;

    private AutoCloseable openMocks;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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
    class RegistrarChegada {

        @Test
        void deveRegistrarChegadaComSucesso() throws Exception {
            RegistroChegadaDTO dto = new RegistroChegadaDTO(1L, false);

            mockMvc.perform(post("/recepcao/registrarChegada")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Paciente registrado com sucesso!"));
        }

        @Test
        void deveRetornar404SePacienteNaoExistir() throws Exception {
            RegistroChegadaDTO dto = new RegistroChegadaDTO(99L, false);

            doThrow(new EntidadeNaoEncontradaException("Paciente não encontrado"))
                    .when(registrarChegadaUseCase).registrarChegada(eq(99L), eq(false));

            mockMvc.perform(post("/recepcao/registrarChegada")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Paciente não encontrado"));
        }
    }
}
