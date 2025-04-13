package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CalcularTempoEstimadoUseCaseTest {

    private CalcularTempoEstimadoUseCase calcularTempoEstimadoUseCase;

    @Mock
    private FilaAtendimentoRepository filaAtendimentoRepository;

    private FilaAtendimento filaAtual;

    private AutoCloseable openMocks;


    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        calcularTempoEstimadoUseCase = new CalcularTempoEstimadoUseCase(filaAtendimentoRepository);

        filaAtual = new FilaAtendimento();
        filaAtual.setId(1L);
        filaAtual.setPesoFila(50);
        filaAtual.setHorarioEntradaFila(LocalDateTime.now().minusMinutes(5));
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }


    @Test
    void deveCalcularTempoEstimadoComPacientesNaFrente() {
        FilaAtendimento paciente1 = new FilaAtendimento();
        paciente1.setId(2L);
        paciente1.setPesoFila(60); // maior peso
        paciente1.setHorarioEntradaFila(LocalDateTime.now().minusMinutes(10));

        FilaAtendimento paciente2 = new FilaAtendimento();
        paciente2.setId(3L);
        paciente2.setPesoFila(50); // mesmo peso
        paciente2.setHorarioEntradaFila(LocalDateTime.now().minusMinutes(7)); // entrou antes

        FilaAtendimento paciente3 = new FilaAtendimento();
        paciente3.setId(4L);
        paciente3.setPesoFila(40); // menor peso, será ignorado

        when(filaAtendimentoRepository.findByStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE))
                .thenReturn(List.of(paciente1, paciente2, paciente3, filaAtual)); // inclui ele mesmo

        LocalTime resultado = calcularTempoEstimadoUseCase.calcularTempoEstimado(filaAtual);

        // 2 pacientes na frente * 10 minutos
        assertEquals(LocalTime.of(0, 20), resultado);
    }

    @Test
    void deveRetornarZeroQuandoNaoHaPacientesNaFrente() {
        FilaAtendimento pacienteComMenorPeso = new FilaAtendimento();
        pacienteComMenorPeso.setId(2L);
        pacienteComMenorPeso.setPesoFila(30); // menor, ignora

        when(filaAtendimentoRepository.findByStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE))
                .thenReturn(List.of(filaAtual, pacienteComMenorPeso));

        LocalTime resultado = calcularTempoEstimadoUseCase.calcularTempoEstimado(filaAtual);

        assertEquals(LocalTime.of(0, 0), resultado);
    }
}