package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.ProfissionalSaudeRequestDTO;
import br.com.hackaton.priorizasus.entities.ProfissionalSaude;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.ProfissionalSaudeRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AtualizarProfissionalUseCaseTest {

    @Mock
    private ProfissionalSaudeRepository repository;;

    private AtualizarProfissionalUseCase atualizarProfissionalUseCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        atualizarProfissionalUseCase = new AtualizarProfissionalUseCase(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    @DisplayName("Atulizar Profissional")
    class BuscarProfissional {

        @Test
        @DisplayName("Deve Atulizar o profissional com sucesso!")
        void deveAtualizarProfissionalComSucesso() {
            ProfissionalSaude profissional = new ProfissionalSaude(1L, "Antigo", "000", "Geral", null);
            ProfissionalSaudeRequestDTO dto = new ProfissionalSaudeRequestDTO("Novo Nome", "111", "Ortopedia",null);

            when(repository.findById(1L)).thenReturn(Optional.of(profissional));

            atualizarProfissionalUseCase.atualizar(1L, dto);

            verify(repository).save(profissional);
            assertEquals("Novo Nome", profissional.getNome());
            assertEquals("111", profissional.getCrm());
            assertEquals("Ortopedia", profissional.getEspecialidade());
        }

        @DisplayName("Deve falhar ao atualizar o profissional por ele não existir!")
        @Test
        void deveLancarExcecaoSeProfissionalNaoExistir() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            ProfissionalSaudeRequestDTO dto = new ProfissionalSaudeRequestDTO("x", "x", "x",null);

            assertThrows(EntidadeNaoEncontradaException.class, () -> atualizarProfissionalUseCase.atualizar(1L, dto));
        }

    }
}

