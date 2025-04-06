package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.PacienteCadastradoDTO;
import br.com.hackaton.priorizasus.dto.PacienteParaCadastrarDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CadastrarPacienteUseCaseTest {

    @Mock
    private PacienteRepository pacienteRepository;

    private CadastrarPacienteUseCase cadastrarPacienteUseCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        cadastrarPacienteUseCase = new CadastrarPacienteUseCase(pacienteRepository);
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    @DisplayName("Cadastrar")
    class Cadastrar {

        @Test
        @DisplayName("Deve adicionar um paciente na fila com sucesso!")
        void deveCadastrarPacienteComSucesso() {
            PacienteParaCadastrarDTO dto = new PacienteParaCadastrarDTO(
                    "João Silva",
                    "12345678900",
                    LocalDateTime.of(1990, 5, 20, 0, 0),
                    "11999999999",
                    "Rua das Flores, 123"
            );

            Paciente pacienteSalvo = Paciente.builder()
                    .id(1L)
                    .nome(dto.nome())
                    .cpf(dto.cpf())
                    .dataNascimento(dto.dataNascimento())
                    .telefone(dto.telefone())
                    .endereco(dto.endereco())
                    .build();

            when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteSalvo);

            PacienteCadastradoDTO resultado = cadastrarPacienteUseCase.cadastrar(dto);

            assertEquals(1L, resultado.pacienteId());
            assertEquals("João Silva", resultado.nomePaciente());
            assertEquals("12345678900", resultado.cpfPaciente());
        }
    }
}

