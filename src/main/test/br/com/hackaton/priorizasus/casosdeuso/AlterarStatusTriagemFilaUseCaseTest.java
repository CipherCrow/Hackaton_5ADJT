package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.entities.FilaTriagem;
import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.FilaTriagemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AlterarStatusTriagemFilaUseCaseTest {

    @Mock
    private FilaTriagemRepository filaTriagemRepository;

    private AlterarStatusFilaTriagemUseCase useCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        useCase = new AlterarStatusFilaTriagemUseCase(filaTriagemRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveAlterarStatusComSucesso() {
        // Arrange
        Long id = 1L;
        FilaTriagem fila = new FilaTriagem();
        fila.setId(id);
        fila.setStatusTriagem(StatusTriagemEnum.AGUARDANDO);

        when(filaTriagemRepository.findById(id)).thenReturn(Optional.of(fila));

        // Act
        useCase.alterarStatus(id, StatusTriagemEnum.TRIAGEM_REALIZADA);

        // Assert
        assertEquals(StatusTriagemEnum.TRIAGEM_REALIZADA, fila.getStatusTriagem());
        verify(filaTriagemRepository).findById(id);
        verify(filaTriagemRepository).save(fila);
    }

    @Test
    void deveLancarExcecaoSeIdNaoForEncontrado() {
        // Arrange
        Long idInvalido = 999L;
        when(filaTriagemRepository.findById(idInvalido)).thenReturn(Optional.empty());

        // Assert
        assertThrows(EntidadeNaoEncontradaException.class, () -> useCase.alterarStatus(idInvalido, StatusTriagemEnum.CANCELADO));
        verify(filaTriagemRepository).findById(idInvalido);
        verify(filaTriagemRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoSeStatusTriagemForDiferenteDeAguardando() {
        // Arrange
        Long id = 1L;
        FilaTriagem fila = new FilaTriagem();
        fila.setId(id);
        fila.setStatusTriagem(StatusTriagemEnum.TRIAGEM_REALIZADA);

        when(filaTriagemRepository.findById(id)).thenReturn(Optional.of(fila));

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                useCase.alterarStatus(id, StatusTriagemEnum.EM_ANDAMENTO)
        );

        assertEquals("Apenas é possível iniciar triagens para quem está aguardando!", exception.getMessage());

        verify(filaTriagemRepository, times(1)).findById(id);
        verify(filaTriagemRepository, never()).save(any()); // Nunca deve salvar
    }
}


