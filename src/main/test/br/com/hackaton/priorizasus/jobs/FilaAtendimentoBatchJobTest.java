package br.com.hackaton.priorizasus.jobs;

import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.entities.Triagem;
import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private FilaAtendimento criar(String nome, NivelPrioridadeEnum prioridade, long minutos) {
        return FilaAtendimento.builder()
                .id((long) nome.hashCode())
                .triagem(prioridade != null ? Triagem.builder().nivelPrioridadeEnum(prioridade).build() : null)
                .atendimentoAdministrativo(false)
                .horarioEntradaFila(LocalDateTime.now().minusMinutes(minutos))
                .statusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE)
                .pesoFila(0) // será calculado
                .build();
    }

    @Test
    void deveAtualizarPesosEOrdenarCorretamente() {
        // Arrange
        FilaAtendimento vermelho = criar("vermelho", NivelPrioridadeEnum.VERMELHO, 3);
        FilaAtendimento laranja = criar("laranja", NivelPrioridadeEnum.LARANJA, 3);
        FilaAtendimento amarelo = criar("amarelo", NivelPrioridadeEnum.AMARELO, 3);
        FilaAtendimento verde = criar("verde", NivelPrioridadeEnum.VERDE, 3);
        FilaAtendimento azul = criar("azul", NivelPrioridadeEnum.AZUL, 3);
        FilaAtendimento adm = criar("adm", null, 3);
        adm.setAtendimentoAdministrativo(true);

        List<FilaAtendimento> fila = Arrays.asList(verde, azul, vermelho, amarelo, adm, laranja);

        when(filaAtendimentoRepository.findByStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE))
                .thenReturn(fila);

        // Act
        job.atualizarPesosPendentes();

        // Assert
        verify(filaAtendimentoRepository, times(1)).saveAll(fila);

        fila.sort((f1, f2) -> Integer.compare(f2.getPesoFila(), f1.getPesoFila())); // ordem decrescente de prioridade

        assertEquals(vermelho.getPesoFila(), fila.get(0).getPesoFila()); // mais alto
        assertEquals(laranja.getPesoFila(), fila.get(1).getPesoFila());
        assertEquals(amarelo.getPesoFila(), fila.get(2).getPesoFila());
        assertEquals(verde.getPesoFila(), fila.get(3).getPesoFila());
        assertEquals(azul.getPesoFila(), fila.get(4).getPesoFila());
        assertEquals(adm.getPesoFila(), fila.get(5).getPesoFila()); // administrativo = mais baixo
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
