package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.FilaTriagemResponseDTO;
import br.com.hackaton.priorizasus.entities.FilaTriagem;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.FilaTriagemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuscarTriagemFilaPorCpfUseCaseTest {

    @Mock
    private FilaTriagemRepository filaTriagemRepository;

    private BuscarPacienteFilaTriagemUseCase useCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        useCase = new BuscarPacienteFilaTriagemUseCase(filaTriagemRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveRetornarPacientePorId() {
        // Arrange
        Long id = 1L;
        FilaTriagem fila = criarFilaTriagem(id, "Paciente 1", "12345678900");

        when(filaTriagemRepository.findById(id)).thenReturn(Optional.of(fila));

        // Act
        FilaTriagemResponseDTO dto = useCase.buscar(id.toString());

        // Assert
        assertNotNull(dto);
        assertEquals("Paciente 1", dto.nomePaciente());
        verify(filaTriagemRepository).findById(id);
    }

    @Test
    void deveRetornarPacientePorCpf() {
        // Arrange
        String cpf = "12345678900";
        FilaTriagem fila = criarFilaTriagem(1L, "Paciente CPF", cpf);

        when(filaTriagemRepository.findByPacienteCpfAndStatusTriagem(cpf,StatusTriagemEnum.AGUARDANDO)).thenReturn(Optional.of(fila));

        // Act
        FilaTriagemResponseDTO dto = useCase.buscar(cpf);

        // Assert
        assertNotNull(dto);
        assertEquals("Paciente CPF", dto.nomePaciente());
        verify(filaTriagemRepository).findByPacienteCpfAndStatusTriagem(cpf,StatusTriagemEnum.AGUARDANDO);
    }

    @Test
    void deveLancarExcecaoSePacienteNaoForEncontrado() {
        // Arrange
        String cpf = "99999999999";
        when(filaTriagemRepository.findByPacienteCpfAndStatusTriagem(cpf,StatusTriagemEnum.AGUARDANDO)).thenReturn(Optional.empty());

        // Assert
        assertThrows(EntidadeNaoEncontradaException.class, () -> useCase.buscar(cpf));
        verify(filaTriagemRepository).findByPacienteCpfAndStatusTriagem(cpf,StatusTriagemEnum.AGUARDANDO);
    }

    private FilaTriagem criarFilaTriagem(Long id, String nome, String cpf) {
        Paciente paciente = new Paciente();
        paciente.setNome(nome);
        paciente.setCpf(cpf);

        FilaTriagem fila = new FilaTriagem();
        fila.setId(id);
        fila.setPaciente(paciente);
        fila.setStatusTriagem(StatusTriagemEnum.AGUARDANDO);
        fila.setHorarioEntrada(LocalDateTime.now());

        return fila;
    }
}


