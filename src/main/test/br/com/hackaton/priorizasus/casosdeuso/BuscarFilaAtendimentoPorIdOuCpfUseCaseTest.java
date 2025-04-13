package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.FilaAtendimentoDTO;
import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.entities.Triagem;
import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class BuscarFilaAtendimentoPorIdOuCpfUseCaseTest {

    @Mock
    private FilaAtendimentoRepository filaAtendimentoRepository;

    private BuscarFilaAtendimentoPorIdOuCpfUseCase useCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        useCase = new BuscarFilaAtendimentoPorIdOuCpfUseCase(filaAtendimentoRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class QuandoBuscarPorCpf {

        @Test
        void deveRetornarDTOQuandoEncontrarFilaPorCpf() {
            String cpf = "12345678901";

            Paciente paciente = new Paciente();
            paciente.setNome("João da Silva");
            paciente.setCpf(cpf);

            Triagem triagem = new Triagem();
            triagem.setPaciente(paciente);
            triagem.setNivelPrioridadeEnum(NivelPrioridadeEnum.AMARELO);

            FilaAtendimento fila = new FilaAtendimento();
            fila.setTriagem(triagem);
            fila.setHorarioEntradaFila(LocalDateTime.now());
            fila.setTempoEsperaEstimado(LocalTime.of(0, 45));
            fila.setStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE);

            when(filaAtendimentoRepository.findByTriagem_Paciente_Cpf(cpf)).thenReturn(Optional.of(fila));

            FilaAtendimentoDTO dto = useCase.executar(cpf);

            assertEquals("João da Silva", dto.nomePaciente());
            assertEquals(NivelPrioridadeEnum.AMARELO, dto.nivelPrioridade());
            assertEquals(StatusAtendimentoEnum.PENDENTE, dto.status());
        }

        @Test
        void deveLancarExcecaoQuandoNaoEncontrarFilaPorCpf() {
            String cpf = "12345678901";
            when(filaAtendimentoRepository.findByTriagem_Paciente_Cpf(cpf)).thenReturn(Optional.empty());

            EntidadeNaoEncontradaException ex = assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> useCase.executar(cpf)
            );

            assertEquals("Fila não encontrada por CPF", ex.getMessage());
        }
    }

    @Nested
    class QuandoBuscarPorId {

        @Test
        void deveRetornarDTOQuandoEncontrarFilaPorId() {
            Long id = 1L;

            Paciente paciente = new Paciente();
            paciente.setNome("Maria Oliveira");

            Triagem triagem = new Triagem();
            triagem.setPaciente(paciente);
            triagem.setNivelPrioridadeEnum(NivelPrioridadeEnum.VERDE);

            FilaAtendimento fila = new FilaAtendimento();
            fila.setTriagem(triagem);
            fila.setHorarioEntradaFila(LocalDateTime.now());
            fila.setTempoEsperaEstimado(LocalTime.of(0, 20));
            fila.setStatusAtendimentoEnum(StatusAtendimentoEnum.EM_ATENDIMENTO);

            when(filaAtendimentoRepository.findById(id)).thenReturn(Optional.of(fila));

            FilaAtendimentoDTO dto = useCase.executar(id.toString());

            assertEquals("Maria Oliveira", dto.nomePaciente());
            assertEquals(NivelPrioridadeEnum.VERDE, dto.nivelPrioridade());
            assertEquals(StatusAtendimentoEnum.EM_ATENDIMENTO, dto.status());
        }

        @Test
        void deveLancarExcecaoQuandoNaoEncontrarFilaPorId() {
            when(filaAtendimentoRepository.findById(99L)).thenReturn(Optional.empty());

            EntidadeNaoEncontradaException ex = assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> useCase.executar("99")
            );

            assertEquals("Fila não encontrada por ID", ex.getMessage());
        }

        @Test
        void deveLancarIllegalArgumentExceptionQuandoFormatoInvalido() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> useCase.executar("cpf_invalido")
            );

            assertEquals("Identificador inválido. Deve ser um CPF (11 dígitos) ou um ID numérico.", ex.getMessage());
        }
    }
}


