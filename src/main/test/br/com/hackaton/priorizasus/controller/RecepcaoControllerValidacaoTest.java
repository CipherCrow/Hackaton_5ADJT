package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.AtualizarPacienteUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarPacienteUseCase;
import br.com.hackaton.priorizasus.casosdeuso.CadastrarPacienteUseCase;
import br.com.hackaton.priorizasus.casosdeuso.RegistrarChegadaUseCase;
import br.com.hackaton.priorizasus.dto.PacienteParaCadastrarDTO;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecepcaoControllerValidacaoTest {

    private MockMvc mockMvc;
    @Mock
    private RegistrarChegadaUseCase registrarChegadaUseCase;
    @Mock
    private CadastrarPacienteUseCase cadastrarPacienteUseCase;
    @Mock
    private BuscarPacienteUseCase buscarPacienteUseCase;
    @Mock
    private AtualizarPacienteUseCase atualizarPacienteUseCase;

    private AutoCloseable openMocks;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        RecepcaoController recepcaoController = new RecepcaoController(
                registrarChegadaUseCase,
                cadastrarPacienteUseCase,
                buscarPacienteUseCase,
                atualizarPacienteUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(recepcaoController).setControllerAdvice(GlobalExceptionHandler.class).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class ValidacaoCadastroPaciente {

        @Test
        void deveRetornar400QuandoNomeEhVazio() throws Exception {
            PacienteParaCadastrarDTO dto = new PacienteParaCadastrarDTO(
                    "", "12345678900", LocalDateTime.now().minusYears(20),
                    "11999999999", "Rua Teste");

            mockMvc.perform(post("/recepcao/cadastrarPaciente")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Nome é obrigatório")));
        }

        @Test
        void deveRetornar400QuandoCpfEhVazio() throws Exception {
            PacienteParaCadastrarDTO dto = new PacienteParaCadastrarDTO(
                    "Galinacio", "", LocalDateTime.now().minusYears(20),
                    "11999999999", "Rua Teste");

            mockMvc.perform(post("/recepcao/cadastrarPaciente")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("CPF é obrigatório")));
        }

        @Test
        void deveRetornar400QuandoDataNascimentoEhFutura() throws Exception {
            PacienteParaCadastrarDTO dto = new PacienteParaCadastrarDTO(
                    "Fulano", "12345678900", LocalDateTime.now().plusDays(1),
                    "11999999999", "Rua Teste");

            mockMvc.perform(post("/recepcao/cadastrarPaciente")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Data de nascimento deve estar no passado")));
        }
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

    @Nested
    class ValidacaoAtualizacaoPaciente {

        @Test
        void deveRetornar400QuandoNomeEhNulo() throws Exception {
            String json = """
                {
                    "nome": "",
                    "cpf": "12345678900",
                    "dataNascimento": "2000-01-01T00:00:00",
                    "telefone": "11999999999",
                    "endereco": "Rua Exemplo"
                }
                """;

            mockMvc.perform(put("/recepcao/atualizarPaciente/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Nome é obrigatório")));
        }

        @Test
        void deveRetornar400QuandoCpfEhVazio() throws Exception {
            String json = """
                {
                    "nome": "Joana",
                    "cpf": "",
                    "dataNascimento": "2000-01-01T00:00:00",
                    "telefone": "11999999999",
                    "endereco": "Rua Exemplo"
                }
                """;

            mockMvc.perform(put("/recepcao/atualizarPaciente/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("CPF é obrigatório")));
        }

        @Test
        void deveRetornar400QuandoDataNascimentoEhFutura() throws Exception {
            String json = """
                {
                    "nome": "Joana",
                    "cpf": "12345678900",
                    "dataNascimento": "2099-01-01T00:00:00",
                    "telefone": "11999999999",
                    "endereco": "Rua Exemplo"
                }
                """;

            mockMvc.perform(put("/recepcao/atualizarPaciente/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Data de nascimento deve estar no passado")));
        }

    }


}
