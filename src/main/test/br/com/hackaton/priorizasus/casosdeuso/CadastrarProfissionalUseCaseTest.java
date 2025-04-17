package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.ProfissionalSaudeDTO;
import br.com.hackaton.priorizasus.dto.ProfissionalSaudeRequestDTO;
import br.com.hackaton.priorizasus.entities.ProfissionalSaude;
import br.com.hackaton.priorizasus.mapper.ProfissionalSaudeMapper;
import br.com.hackaton.priorizasus.repository.ProfissionalSaudeRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CadastrarProfissionalUseCaseTest {

    @Mock
    private ProfissionalSaudeRepository repository;;

    private CadastrarProfissionalUseCase cadastrarProfissionalUseCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        cadastrarProfissionalUseCase = new CadastrarProfissionalUseCase(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    @DisplayName("Cadastrar")
    class Cadastrar {

        @Test
        @DisplayName("Deve criar um profissional com sucesso!")
        void deveCadastrarPacienteComSucesso() {
            ProfissionalSaudeRequestDTO dto =
                    new ProfissionalSaudeRequestDTO("Dra. Ana", "1234", "Cardiologia",null);
            ProfissionalSaude salvo = ProfissionalSaudeMapper.toEntity(dto);
            salvo.setId(1L);

            when(repository.save(any(ProfissionalSaude.class))).thenReturn(salvo);

            ProfissionalSaudeDTO resultado = cadastrarProfissionalUseCase.cadastrar(dto);

            assertEquals("Dra. Ana", resultado.nome());
            assertEquals("1234", resultado.crm());
            assertEquals("Cardiologia", resultado.especialidade());
        }
    }
}

