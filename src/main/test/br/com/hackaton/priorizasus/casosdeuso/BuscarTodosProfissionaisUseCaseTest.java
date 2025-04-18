package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.ProfissionalSaudeDTO;
import br.com.hackaton.priorizasus.entities.ProfissionalSaude;
import br.com.hackaton.priorizasus.repository.ProfissionalSaudeRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BuscarTodosProfissionaisUseCaseTest {

    @Mock
    private ProfissionalSaudeRepository repository;

    private BuscarTodosProfissionaisUseCase buscarTodosProfissionaisUseCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        buscarTodosProfissionaisUseCase = new BuscarTodosProfissionaisUseCase(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    @DisplayName("Buscar Todos")
    class BuscarProfissionais {

        @Test
        @DisplayName("Deve Buscar todos os profissionais com sucesso!")
        void deveBuscarProfissionaisComSucesso() {
            List<ProfissionalSaude> lista = List.of(
                    new ProfissionalSaude(1L, "Dr. João", "123", "Clínico Geral", null),
                    new ProfissionalSaude(2L, "Dra. Carla", "456", "Pediatria", null)
            );

            when(repository.findAll()).thenReturn(lista);

            List<ProfissionalSaudeDTO> resultado = buscarTodosProfissionaisUseCase.buscarTodos();

            assertEquals(2, resultado.size());
            assertEquals("Dr. João", resultado.get(0).nome());
            assertEquals("456", resultado.get(1).crm());
        }
    }
}

