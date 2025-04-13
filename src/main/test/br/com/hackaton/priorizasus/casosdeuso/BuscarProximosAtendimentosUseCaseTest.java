package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.FilaAtendimentoDTO;
import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.entities.Triagem;
import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class BuscarProximosAtendimentosUseCaseTest {

    @Mock
    private FilaAtendimentoRepository filaAtendimentoRepository;

    private BuscarProximosAtendimentosUseCase useCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        useCase = new BuscarProximosAtendimentosUseCase(filaAtendimentoRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class QuandoBuscarProximosAtendimentos {

        @Test
        void deveRetornarListaDeDTOsOrdenadaComPrioridadeAbsolutaParaVermelhoELaranja() {
            FilaAtendimento pacienteVermelho = criarFila(NivelPrioridadeEnum.VERMELHO, 100);
            pacienteVermelho.getTriagem().getPaciente().setNome("Paciente Vermelho");

            FilaAtendimento pacienteLaranja = criarFila(NivelPrioridadeEnum.LARANJA, 200);
            pacienteLaranja.getTriagem().getPaciente().setNome("Paciente Laranja");

            FilaAtendimento pacienteAmarelo = criarFila(NivelPrioridadeEnum.AMARELO, 999);
            pacienteAmarelo.getTriagem().getPaciente().setNome("Paciente Amarelo");

            List<FilaAtendimento> lista = List.of(pacienteAmarelo, pacienteLaranja, pacienteVermelho);

            when(filaAtendimentoRepository.findByStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE)).thenReturn(lista);

            List<FilaAtendimentoDTO> resultado = useCase.executar();

            assertEquals(3, resultado.size());
            assertEquals("Paciente Vermelho", resultado.get(0).nomePaciente());
            assertEquals("Paciente Laranja", resultado.get(1).nomePaciente());
            assertEquals("Paciente Amarelo", resultado.get(2).nomePaciente());
        }

        @Test
        void deveRetornarListaVaziaQuandoNaoHouverPendentes() {
            when(filaAtendimentoRepository.findByStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE))
                    .thenReturn(Collections.emptyList());

            List<FilaAtendimentoDTO> resultado = useCase.executar();

            assertTrue(resultado.isEmpty());
        }

        private FilaAtendimento criarFila(NivelPrioridadeEnum prioridade, int pesoFila) {
            Paciente paciente = new Paciente();
            paciente.setNome("Paciente");

            Triagem triagem = new Triagem();
            triagem.setPaciente(paciente);
            triagem.setNivelPrioridadeEnum(prioridade);

            FilaAtendimento fila = new FilaAtendimento();
            fila.setTriagem(triagem);
            fila.setStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE);
            fila.setPesoFila(pesoFila);
            fila.setHorarioEntradaFila(LocalDateTime.now());
            fila.setTempoEsperaEstimado(LocalTime.of(0, 30));

            return fila;
        }
    }
}


