package br.com.hackaton.priorizasus.jobs;

import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class FilaAtendimentoBatchJobTest {

    private AutoCloseable openMocks;
    @Mock
    private FilaAtendimentoRepository filaAtendimentoRepository;

    private FilaAtendimentoBatchJob job;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        job = new FilaAtendimentoBatchJob(filaAtendimentoRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }


    @Test
    void deveAtualizarPesoFilaParaTodosPendentes() {
        // Arrange
        FilaAtendimento fila1 = FilaAtendimento.builder()
                .id(1L)
                .horarioEntradaFila(LocalDateTime.now().minusMinutes(10))
                .atendimentoAdministrativo(true)
                .statusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE)
                .build();

        FilaAtendimento fila2 = FilaAtendimento.builder()
                .id(2L)
                .horarioEntradaFila(LocalDateTime.now().minusMinutes(20))
                .atendimentoAdministrativo(true)
                .statusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE)
                .build();

        List<FilaAtendimento> pendentes = List.of(fila1, fila2);
        when(filaAtendimentoRepository.findByStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE))
                .thenReturn(pendentes);

        // Act
        job.atualizarPesosPendentes();

        // Assert
        verify(filaAtendimentoRepository, times(1)).saveAll(pendentes);
        assertThat(fila1.getPesoFila()).isGreaterThan(0);
        assertThat(fila2.getPesoFila()).isGreaterThan(fila1.getPesoFila()); // supondo maior tempo de espera
    }
}
