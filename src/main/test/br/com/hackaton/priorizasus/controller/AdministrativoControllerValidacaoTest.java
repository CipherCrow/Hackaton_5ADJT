package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdministrativoControllerValidacaoTest {

    private MockMvc mockMvc;
    @Mock
    private CadastrarPacienteUseCase cadastrarPacienteUseCase;
    @Mock
    private BuscarPacienteUseCase buscarPacienteUseCase;
    @Mock
    private AtualizarPacienteUseCase atualizarPacienteUseCase;
    @Mock
    private CadastrarSintomaUseCase cadastrarSintomaUseCase;
    @Mock
    private VisualizarTodosOsSintomasUseCase visualizarTodosOsSintomasUseCase;
    @Mock
    private VisualizarSintomaPorIdUseCase  visualizarSintomaPorIdUseCase;
    @Mock
    private AtualizarSintomaUseCase atualizarSintomaUseCase;

    private AutoCloseable openMocks;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        AdministrativoController administrativoController = new AdministrativoController(
                cadastrarPacienteUseCase,
                buscarPacienteUseCase,
                atualizarPacienteUseCase,
                cadastrarSintomaUseCase,
                visualizarTodosOsSintomasUseCase,
                visualizarSintomaPorIdUseCase,
                atualizarSintomaUseCase
        );
        mockMvc = MockMvcBuilders.standaloneSetup(administrativoController).setControllerAdvice(GlobalExceptionHandler.class).build();
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

            mockMvc.perform(post("/administrativo/cadastrarPaciente")
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

            mockMvc.perform(post("/administrativo/cadastrarPaciente")
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

            mockMvc.perform(post("/administrativo/cadastrarPaciente")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Data de nascimento deve estar no passado")));
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

            mockMvc.perform(put("/administrativo/atualizarPaciente/1")
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

            mockMvc.perform(put("/administrativo/atualizarPaciente/1")
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

            mockMvc.perform(put("/administrativo/atualizarPaciente/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Data de nascimento deve estar no passado")));
        }

    }

    @Nested
    class ValidacaoCadastroSintoma {

        @Test
        void deveRetornar400QuandoDescricaoEstiverEmBranco() throws Exception {
            String json = """
                {
                    "descricao": " ",
                    "gravidade": 3
                }
                """;

            mockMvc.perform(post("/administrativo/cadastrarSintomas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Deve existir uma descrição para o sintoma")));
        }

        @Test
        void deveRetornar400QuandoGravidadeForMenorQue1() throws Exception {
            String json = """
                {
                    "descricao": "Febre alta",
                    "gravidade": 0
                }
                """;

            mockMvc.perform(post("/administrativo/cadastrarSintomas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Deve ter pelo menos 1 de gravidade")));
        }

        @Test
        void deveRetornar400QuandoGravidadeForMaiorQue5() throws Exception {
            String json = """
                {
                    "descricao": "Dor insuportável",
                    "gravidade": 6
                }
                """;

            mockMvc.perform(post("/administrativo/cadastrarSintomas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Deve ter no maximo 5 de gravidade")));
        }
    }

}
