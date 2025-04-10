package br.com.hackaton.priorizasus.casosdeuso;


import br.com.hackaton.priorizasus.dto.SintomaDTO;
import br.com.hackaton.priorizasus.dto.SintomaRequestDTO;
import br.com.hackaton.priorizasus.entities.Sintoma;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.SintomaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SintomaUseCasesTest {

    @Mock
    private SintomaRepository repository;

    private CadastrarSintomaUseCase cadastrar;

    private VisualizarTodosOsSintomasUseCase visualizarTodos;

    private VisualizarSintomaPorIdUseCase visualizarPorId;

    private AtualizarSintomaUseCase atualizar;

    private Sintoma sintoma;

    private AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        cadastrar = new CadastrarSintomaUseCase(repository);
        visualizarTodos = new VisualizarTodosOsSintomasUseCase(repository);
        visualizarPorId = new VisualizarSintomaPorIdUseCase(repository);
        atualizar = new AtualizarSintomaUseCase(repository);

        sintoma = Sintoma.builder()
                .id(1L)
                .descricao("Febre")
                .gravidade(3)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class CadastrarSintoma {

        @Test
        void deveCadastrarSintomaComSucesso() {
            SintomaRequestDTO dto = new SintomaRequestDTO("Febre", 3);

            when(repository.save(any())).thenReturn(sintoma);

            SintomaDTO resultado = cadastrar.cadastrarSintoma(dto);

            assertEquals(1L, resultado.id());
            assertEquals("Febre", resultado.descricao());
            assertEquals(3, resultado.gravidade());
        }
    }

    @Nested
    class VisualizarTodos {

        @Test
        void deveListarTodosOsSintomas() {
            when(repository.findAll()).thenReturn(List.of(sintoma));

            List<SintomaDTO> sintomas = visualizarTodos.buscarTodosOsSintomas();

            assertEquals(1, sintomas.size());
            assertEquals("Febre", sintomas.get(0).descricao());
        }
    }

    @Nested
    class VisualizarPorId {

        @Test
        void deveRetornarSintomaPorId() {
            when(repository.findById(1L)).thenReturn(Optional.of(sintoma));

            SintomaDTO dto = visualizarPorId.buscarSintoma(1L);

            assertEquals("Febre", dto.descricao());
        }

        @Test
        void deveLancarExcecaoSeSintomaNaoExistir() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(EntidadeNaoEncontradaException.class, () -> visualizarPorId.buscarSintoma(1L));
        }
    }

    @Nested
    class Atualizar {

        @Test
        void deveAtualizarSintomaComSucesso() {
            SintomaRequestDTO dto = new SintomaRequestDTO("Tosse", 2);

            when(repository.findById(1L)).thenReturn(Optional.of(sintoma));

            atualizar.atualizar(1L, dto);

            verify(repository).save(argThat(s ->
                    s.getDescricao().equals("Tosse") && s.getGravidade() == 2
            ));
        }

        @Test
        void deveLancarExcecaoSeSintomaNaoExistir() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            SintomaRequestDTO dto = new SintomaRequestDTO("Dor", 1);

            assertThrows(EntidadeNaoEncontradaException.class, () -> atualizar.atualizar(1L, dto));
        }
    }
}

