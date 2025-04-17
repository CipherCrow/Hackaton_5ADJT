package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.CadastrarPacientePublicamenteUseCase;
import br.com.hackaton.priorizasus.dto.PacienteCadastroPublicoDTO;
import br.com.hackaton.priorizasus.exception.EntidadeJaExisteException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PacientePublicoControllerTest {

    private MockMvc mockMvc;

    private PacientePublicoController controller;

    @Mock
    private CadastrarPacientePublicamenteUseCase useCase;

    private AutoCloseable closeable;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        controller = new PacientePublicoController(useCase);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void deveCadastrarPacienteComSucesso() throws Exception {
        // Arrange
        PacienteCadastroPublicoDTO dto = new PacienteCadastroPublicoDTO(
                "Ana Maria",
                "12345678900",
                LocalDateTime.of(1990, 5, 10, 0, 0),
                "11988887777",
                "Rua das Flores",
                "ana_login",
                "senhaSegura123"
        );

        mockMvc.perform(post("/publico/pacientes/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(useCase).executar(dto);
    }

    @Test
    void deveRetornar400QuandoDadosSaoInvalidos() throws Exception {
        PacienteCadastroPublicoDTO dto = new PacienteCadastroPublicoDTO(
                "", // nome inválido
                "", // cpf inválido
                LocalDateTime.now().plusDays(1), // data futura
                "11988887777",
                "Rua das Flores",
                "", // login inválido
                ""  // senha inválida
        );

        mockMvc.perform(post("/publico/pacientes/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").value("Nome é obrigatório."))
                .andExpect(jsonPath("$.cpf").value("CPF é obrigatório."))
                .andExpect(jsonPath("$.dataNascimento").value("Data de nascimento deve ser no passado."))
                .andExpect(jsonPath("$.login").value("Login é obrigatório."))
                .andExpect(jsonPath("$.senha").value("Senha é obrigatória."));

        verify(useCase, never()).executar(any());
    }

    @Test
    void deveRetornar409QuandoLoginOuCpfJaExistirem() throws Exception {
        PacienteCadastroPublicoDTO dto = new PacienteCadastroPublicoDTO(
                "João",
                "98765432100",
                LocalDateTime.of(1985, 3, 15, 0, 0),
                "11977776666",
                "Av. Principal",
                "joao123",
                "senhaJoao"
        );

        doThrow(new EntidadeJaExisteException("Já existe um paciente com esse CPF."))
                .when(useCase).executar(dto);

        mockMvc.perform(post("/publico/pacientes/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Já existe um paciente com esse CPF."));
    }
}
