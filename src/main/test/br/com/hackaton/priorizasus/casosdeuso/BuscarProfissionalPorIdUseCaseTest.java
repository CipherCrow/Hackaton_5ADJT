package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.ProfissionalSaudeDTO;
import br.com.hackaton.priorizasus.entities.ProfissionalSaude;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.ProfissionalSaudeRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class BuscarProfissionalPorIdUseCaseTest {

    @Mock
    private ProfissionalSaudeRepository repository;;

    private BuscarProfissionalPorIdUseCase buscarProfissionalPorIdUseCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        buscarProfissionalPorIdUseCase = new BuscarProfissionalPorIdUseCase(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    @DisplayName("Buscar Profissional Por ID")
    class BuscarProfissional {

        @Test
        @DisplayName("Deve Buscar o profissional com sucesso!")
        void deveBuscarProfissionaisComSucesso() throws Exception {
            ProfissionalSaude profissional = new ProfissionalSaude(1L, "Dr. Pedro", "555", "Neurologia", null);
            when(repository.findById(1L)).thenReturn(Optional.of(profissional));

            ProfissionalSaudeDTO resultado = buscarProfissionalPorIdUseCase.buscarPorId(1L);

            assertEquals("Dr. Pedro", resultado.nome());
            assertEquals("Neurologia", resultado.especialidade());
        }

        @DisplayName("Deve falhar ao buscar o profissional por ele não existir!")
        @Test
        void deveLancarExcecaoQuandoProfissionalNaoForEncontrado() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(EntidadeNaoEncontradaException.class, () -> buscarProfissionalPorIdUseCase.buscarPorId(99L));
        }

    }
}

