package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.*;
import br.com.hackaton.priorizasus.dto.PacienteCadastradoDTO;
import br.com.hackaton.priorizasus.dto.PacienteParaCadastrarDTO;
import br.com.hackaton.priorizasus.dto.SintomaDTO;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdministrativoControllerTest {

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
    class CadastrarPaciente {

        @Test
        void deveCadastrarPacienteComSucesso() throws Exception {
            PacienteParaCadastrarDTO dto = new PacienteParaCadastrarDTO(
                    "João da Silva", "12345678900", LocalDateTime.now().minusYears(20),
                    "11999999999", "Rua Teste, 123");

            PacienteCadastradoDTO retorno = new PacienteCadastradoDTO(1L, "João da Silva", "12345678900");

            when(cadastrarPacienteUseCase.cadastrar(any())).thenReturn(retorno);

            mockMvc.perform(post("/administrativo/cadastrarPaciente")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.pacienteId").value(1L));
        }
    }

    @Nested
    class BuscarPaciente {

        @Test
        void deveBuscarPacienteComSucessoId() throws Exception {
            PacienteCadastradoDTO retorno = new PacienteCadastradoDTO(1L, "Maria", "11122233344");

            when(buscarPacienteUseCase.buscarPorId(1L)).thenReturn(retorno);

            mockMvc.perform(get("/administrativo/buscarPacienteId/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nomePaciente").value("Maria"));
        }

        @Test
        void deveRetornar404SePacienteNaoForEncontradoId() throws Exception {
            when(buscarPacienteUseCase.buscarPorId(1L))
                    .thenThrow(new EntidadeNaoEncontradaException("Paciente não encontrado"));

            mockMvc.perform(get("/administrativo/buscarPacienteId/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Paciente não encontrado"));
        }

        @Test
        void deveBuscarPacienteComSucessoCpf() throws Exception {
            PacienteCadastradoDTO retorno = new PacienteCadastradoDTO(1L, "Maria", "12345678900");

            when(buscarPacienteUseCase.buscarPorCpf("12345678900")).thenReturn(retorno);

            mockMvc.perform(get("/administrativo/buscarPacienteCpf/12345678900"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nomePaciente").value("Maria"));
        }

        @Test
        void deveRetornar404SePacienteNaoForEncontradoCpf() throws Exception {
            when(buscarPacienteUseCase.buscarPorCpf("12345678900"))
                    .thenThrow(new EntidadeNaoEncontradaException("Paciente não encontrado"));

            mockMvc.perform(get("/administrativo/buscarPacienteCpf/12345678900"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Paciente não encontrado"));
        }
    }

    @Nested
    class AtualizarPaciente {

        @Test
        void deveAtualizarPacienteComSucesso() throws Exception {
            PacienteParaCadastrarDTO dto = new PacienteParaCadastrarDTO(
                    "Novo Nome", "98765432100", LocalDateTime.now().minusYears(25),
                    "11988887777", "Nova Rua");

            mockMvc.perform(put("/administrativo/atualizarPaciente/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Paciente atualizado com sucesso!"));
        }

        @Test
        void deveRetornar404AoAtualizarPacienteInexistente() throws Exception {
            PacienteParaCadastrarDTO dto = new PacienteParaCadastrarDTO(
                    "Novo Nome", "98765432100", LocalDateTime.now().minusYears(25),
                    "11988887777", "Nova Rua");

            doThrow(new EntidadeNaoEncontradaException("Paciente não encontrado"))
                    .when(atualizarPacienteUseCase).atualizar(eq(1L), any());

            mockMvc.perform(put("/administrativo/atualizarPaciente/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Paciente não encontrado"));
        }
    }

    @Nested
    class CadastrarSintoma {

        @Test
        void deveCadastrarSintoma() throws Exception {
            SintomaDTO response = new SintomaDTO(1L, "Dor", 3);

            when(cadastrarSintomaUseCase.cadastrarSintoma(any())).thenReturn(response);

            mockMvc.perform(post("/administrativo/cadastrarSintomas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                        {
                          "descricao": "Dor",
                          "gravidade": 3
                        }
                    """))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.descricao").value("Dor"));
        }

    }

    @Nested
    class VisualizarTodosSintomas {

        @Test
        void deveRetornarTodosSintomas() throws Exception {
            when(visualizarTodosOsSintomasUseCase.buscarTodosOsSintomas()).thenReturn(
                    List.of(new SintomaDTO(1L, "Febre", 3))
            );

            mockMvc.perform(get("/administrativo/Buscarsintomas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].descricao").value("Febre"));
        }
    }

    @Nested
    class VisualizarSintomaPorID {

        @Test
        void deveRetornarSintomaPorId() throws Exception {
            when(visualizarSintomaPorIdUseCase.buscarSintoma(1L)).thenReturn(
                    new SintomaDTO(1L, "Febre", 3)
            );

            mockMvc.perform(get("/administrativo/BuscarSintoma/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.descricao").value("Febre"));
        }

        @Test
        void deveRetornarErro404SeSintomaNaoExistir() throws Exception {
            when(visualizarSintomaPorIdUseCase.buscarSintoma(99L)).thenThrow(
                    new EntidadeNaoEncontradaException("Sintoma não encontrado")
            );

            mockMvc.perform(get("/administrativo/BuscarSintoma/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Sintoma não encontrado"));
        }
    }

    @Nested
    class AtualizarSintoma {

        @Test
        void deveAtualizarSintoma() throws Exception {
            doNothing().when(atualizarSintomaUseCase).atualizar(eq(1L), any());

            mockMvc.perform(put("/administrativo/atualizarSintoma/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                        {
                          "descricao": "Tosse",
                          "gravidade": 2
                        }
                    """))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Sintoma atualizado com sucesso!"));
        }

        @Test
        void deveRetornarErroSeSintomaNaoExistir() throws Exception {
            doThrow(new EntidadeNaoEncontradaException("Sintoma não encontrado"))
                    .when(atualizarSintomaUseCase).atualizar(eq(1L), any());

            mockMvc.perform(put("/administrativo/atualizarSintoma/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                        {
                          "descricao": "Tosse",
                          "gravidade": 2
                        }
                    """))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Sintoma não encontrado"));
        }
    }
}
