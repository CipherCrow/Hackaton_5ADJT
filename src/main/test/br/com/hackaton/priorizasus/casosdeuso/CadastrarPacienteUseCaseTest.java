package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.PacienteCadastradoDTO;
import br.com.hackaton.priorizasus.dto.PacienteParaCadastrarDTO;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.exception.EntidadeJaExisteException;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CadastrarPacienteUseCaseTest {

    @Mock
    private PacienteRepository pacienteRepository;

    private CadastrarPacienteUseCase cadastrarPacienteUseCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        cadastrarPacienteUseCase = new CadastrarPacienteUseCase(pacienteRepository);
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
                    LocalDate.of(1990, 5, 20),
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

        @Test
        @DisplayName("Deve validar que já existe paciente com o cpf!")
        void deveImpedirCadastrarPacienteQueJaExistaNoSistema(){

            PacienteParaCadastrarDTO dto = new PacienteParaCadastrarDTO(
                    "João Silva",
                    "12345678900",
                    LocalDate.of(1990, 5, 20),
                    "11999999999",
                    "Rua das Flores, 123"
            );

            when(pacienteRepository.findByCpf("12345678900"))
                    .thenReturn(Optional.of(mock(Paciente.class)));

            EntidadeJaExisteException ex = assertThrows(EntidadeJaExisteException.class,
                    () -> cadastrarPacienteUseCase.cadastrar(dto));

            assertEquals("Já existe um paciente cadastrado com este cpf!", ex.getMessage());
            verify(pacienteRepository, never()).save(any());
        }
    }
}

