package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AtualizarStatusAtendimentoUseCaseTest {

    @Mock
    private FilaAtendimentoRepository repository;

    private AtualizarStatusAtendimentoUseCase useCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        useCase = new AtualizarStatusAtendimentoUseCase(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveAtualizarStatusComSucesso() {
        FilaAtendimento fila = new FilaAtendimento();
        fila.setStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE);

        when(repository.findById(1L)).thenReturn(Optional.of(fila));

        useCase.executar(1L, StatusAtendimentoEnum.EM_ATENDIMENTO);

        assertEquals(StatusAtendimentoEnum.EM_ATENDIMENTO, fila.getStatusAtendimentoEnum());
        verify(repository).save(fila);
    }

    @Test
    void deveLancarExcecaoSeFilaNaoForEncontrada() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class, () -> {
            useCase.executar(99L, StatusAtendimentoEnum.CONCLUIDO);
        });

        assertEquals("Fila não encontrada", ex.getMessage());
    }
}


