package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.FinalizarAtendimentoDTO;
import br.com.hackaton.priorizasus.entities.AtendimentoHistorico;
import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.entities.ProfissionalSaude;
import br.com.hackaton.priorizasus.entities.Triagem;
import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.AtendimentoHistoricoRepository;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import br.com.hackaton.priorizasus.repository.ProfissionalSaudeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FinalizarAtendimentoUseCaseTest {

    @Mock
    private FilaAtendimentoRepository filaAtendimentoRepository;

    @Mock
    private AtendimentoHistoricoRepository atendimentoHistoricoRepository;

    @Mock
    private ProfissionalSaudeRepository profissionalSaudeRepository;

    private FinalizarAtendimentoUseCase  useCase;

    private AutoCloseable openMocks;

    private Triagem triagem;
    private ProfissionalSaude profissional;
    private FilaAtendimento fila;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        useCase = new FinalizarAtendimentoUseCase (filaAtendimentoRepository,atendimentoHistoricoRepository,profissionalSaudeRepository);

        triagem = Triagem.builder()
                .id(1L)
                .nivelPrioridadeEnum(NivelPrioridadeEnum.VERMELHO)
                .build();

        profissional = ProfissionalSaude.builder()
                .id(1L)
                .nome("Dr. House")
                .build();

        fila = FilaAtendimento.builder()
                .id(1L)
                .triagem(triagem)
                .statusAtendimentoEnum(StatusAtendimentoEnum.EM_ATENDIMENTO)
                .atendimentoAdministrativo(false)
                .horarioEntradaFila(LocalDateTime.now().minusMinutes(20))
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class QuandoFinalizaComSucesso {

        @Test
        void deveFinalizarAtendimentoComSucesso() {
            FinalizarAtendimentoDTO dto = new FinalizarAtendimentoDTO(
                    fila.getId(),
                    profissional.getId(),
                    "Gripe comum",
                    "Repouso e hidratação"
            );

            when(filaAtendimentoRepository.findById(dto.idAtendimento())).thenReturn(Optional.of(fila));
            when(profissionalSaudeRepository.findById(dto.idProfissional())).thenReturn(Optional.of(profissional));

            useCase.executar(dto);

            assertEquals(StatusAtendimentoEnum.CONCLUIDO, fila.getStatusAtendimentoEnum());
            verify(filaAtendimentoRepository).save(fila);
            verify(atendimentoHistoricoRepository).save(any(AtendimentoHistorico.class));
        }
    }

    @Nested
    class QuandoOcorrerErro {

        @Test
        void deveLancarExcecaoQuandoAtendimentoNaoExiste() {
            FinalizarAtendimentoDTO dto = new FinalizarAtendimentoDTO(
                    999L,
                    profissional.getId(),
                    "Diagnóstico",
                    "Prescrição"
            );

            when(filaAtendimentoRepository.findById(dto.idAtendimento())).thenReturn(Optional.empty());

            EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                    () -> useCase.executar(dto));

            assertEquals("Atendimento não encontrado", ex.getMessage());
        }

        @Test
        void deveLancarExcecaoQuandoProfissionalNaoExiste() {
            FinalizarAtendimentoDTO dto = new FinalizarAtendimentoDTO(
                    fila.getId(),
                    999L,
                    "Diagnóstico",
                    "Prescrição"
            );

            when(filaAtendimentoRepository.findById(dto.idAtendimento())).thenReturn(Optional.of(fila));
            when(profissionalSaudeRepository.findById(dto.idProfissional())).thenReturn(Optional.empty());

            EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                    () -> useCase.executar(dto));

            assertEquals("Profissional não encontrado", ex.getMessage());
        }

        @Test
        void deveLancarExcecaoQuandoStatusNaoForPendente() {
            fila.setStatusAtendimentoEnum(StatusAtendimentoEnum.CONCLUIDO);

            FinalizarAtendimentoDTO dto = new FinalizarAtendimentoDTO(
                    fila.getId(),
                    profissional.getId(),
                    "Diagnóstico",
                    "Prescrição"
            );

            when(filaAtendimentoRepository.findById(dto.idAtendimento())).thenReturn(Optional.of(fila));

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> useCase.executar(dto));

            assertEquals("Apenas atendimentos em atendimento podem ser finalizados", ex.getMessage());
        }
    }
}


