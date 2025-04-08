package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.*;
import br.com.hackaton.priorizasus.dto.*;
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
    @Mock
    private CadastrarProfissionalUseCase cadastrarProfissional;
    @Mock
    private BuscarTodosProfissionaisUseCase buscarTodosProfissionaisUseCase;
    @Mock
    private BuscarProfissionalPorIdUseCase buscarProfissionalPorIdUseCase;
    @Mock
    private AtualizarProfissionalUseCase atualizarProfissionalUseCase;

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
                atualizarSintomaUseCase,
                cadastrarProfissional,
                buscarTodosProfissionaisUseCase,
                buscarProfissionalPorIdUseCase,
                atualizarProfissionalUseCase
                );
        mockMvc = MockMvcBuilders.standaloneSetup(administrativoController).setControllerAdvice(GlobalExceptionHandler.class).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
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

    @Nested
    class CadastrarProfissional {

        @Test
        void deveCadastrarComSucesso() throws Exception {
            ProfissionalSaudeDTO response =
                    new ProfissionalSaudeDTO(1L, "João", "12345", "Cardiologia");

            when(cadastrarProfissional.cadastrar(any())).thenReturn(response);

            String json = """
                {
                    "nome": "João",
                    "crm": "12345",
                    "especialidade": "Cardiologia"
                }
                """;

            mockMvc.perform(post("/administrativo/cadastrarProfissional")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.nome").value("João"))
                    .andExpect(jsonPath("$.crm").value("12345"))
                    .andExpect(jsonPath("$.especialidade").value("Cardiologia"));

            verify(cadastrarProfissional).cadastrar(any());
        }
    }

    @Nested
    class BuscarTodosProfissionais {

        @Test
        void deveRetornarListaDeProfissionais() throws Exception {
            List<ProfissionalSaudeDTO> lista = List.of(
                    new ProfissionalSaudeDTO(1L, "Maria", "98765", "Pediatria")
            );

            when(buscarTodosProfissionaisUseCase.buscarTodos()).thenReturn(lista);

            mockMvc.perform(get("/administrativo/buscarTodosProfissionais"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nome").value("Maria"));

            verify(buscarTodosProfissionaisUseCase).buscarTodos();
        }
    }

    @Nested
    class BuscarProfissionalPorId {

        @Test
        void deveRetornarProfissionalPeloId() throws Exception {
            ProfissionalSaudeDTO response = new ProfissionalSaudeDTO(1L, "Carlos", "11111", "Dermatologia");

            when(buscarProfissionalPorIdUseCase.buscarPorId(1L)).thenReturn(response);

            mockMvc.perform(get("/administrativo/buscarProfissional/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("Carlos"));

            verify(buscarProfissionalPorIdUseCase).buscarPorId(1L);
        }

        @Test
        void deveRetornarErro404SeProfissionalNaoExistir() throws Exception {
            when(buscarProfissionalPorIdUseCase.buscarPorId(99L)).thenThrow(
                    new EntidadeNaoEncontradaException("Profissional não encontrado")
            );

            mockMvc.perform(get("/administrativo/buscarProfissional/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Profissional não encontrado"));
        }
    }

    @Nested
    class AtualizarProfissional {

        @Test
        void deveAtualizarComSucesso() throws Exception {
            String json = """
                {
                    "nome": "Joana",
                    "crm": "55555",
                    "especialidade": "Ortopedia"
                }
                """;

            mockMvc.perform(put("/administrativo/atualizarProfissional/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Profissional atualizado com sucesso!"));

            verify(atualizarProfissionalUseCase).atualizar(eq(1L), any(ProfissionalSaudeRequestDTO.class));
        }

    }
}
