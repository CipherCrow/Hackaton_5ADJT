package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.HistoricoAtendimentoDTO;
import br.com.hackaton.priorizasus.entities.*;
import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import br.com.hackaton.priorizasus.enums.PermissaoEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.mapper.HistoricoAtendimentoMapper;
import br.com.hackaton.priorizasus.repository.AtendimentoHistoricoRepository;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class BuscarHistoricoAtendimentosUseCaseTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private AtendimentoHistoricoRepository atendimentoHistoricoRepository;

    private BuscarHistoricoAtendimentosUseCase useCase;

    private AutoCloseable openMocks;

    private final String login = "paciente01";

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        useCase = new BuscarHistoricoAtendimentosUseCase(pacienteRepository, atendimentoHistoricoRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class QuandoPacienteExiste {

        @Test
        void deveRetornarListaDeHistoricoAtendimentoDTO() {
            Paciente paciente = Paciente.builder()
                    .id(1L)
                    .nome("Fulano")
                    .usuario(new Usuario(10L, login, "senha123", PermissaoEnum.PACIENTE))
                    .build();

            Triagem triagem = Triagem.builder()
                    .id(1L)
                    .paciente(paciente)
                    .sintomas(Set.of(new Sintoma(1L, "Febre", 3)))
                    .nivelPrioridadeEnum(NivelPrioridadeEnum.AMARELO)
                    .profissional(ProfissionalSaude.builder().id(1L).nome("Dr. Carlos").crm("CRM123").build())
                    .build();

            AtendimentoHistorico atendimento = new AtendimentoHistorico(
                    1L,
                    triagem,
                    "Diagnóstico",
                    "Prescrição",
                    LocalDateTime.of(2024, 4,1,14,0),
                    triagem.getProfissional()
            );

            when(pacienteRepository.findByUsuario_Login(login)).thenReturn(Optional.of(paciente));
            when(atendimentoHistoricoRepository.findByTriagem_Paciente_Id(paciente.getId()))
                    .thenReturn(List.of(atendimento));

            // Act
            List<HistoricoAtendimentoDTO> result = useCase.executar(login);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());

            HistoricoAtendimentoDTO dto = result.get(0);
            assertEquals("2024-04-01T14:00", dto.dataAtendimento());
            assertEquals("Dr. Carlos", dto.profissional());
            assertEquals("Febre", dto.sintomas());
            assertEquals("Diagnóstico", dto.diagnostico());
            assertEquals("Prescrição", dto.prescricao());
            assertEquals("AMARELO", dto.prioridade());
        }
    }


    @Nested
    class QuandoPacienteNaoExiste {

        @Test
        void deveLancarExcecaoQuandoPacienteNaoForEncontrado() {
            // Arrange
            when(pacienteRepository.findByUsuario_Login(login)).thenReturn(Optional.empty());

            // Act & Assert
            EntidadeNaoEncontradaException ex = assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> useCase.executar(login)
            );

            assertEquals("Paciente não encontrado para o login informado", ex.getMessage());
            verifyNoInteractions(atendimentoHistoricoRepository);
        }
    }
}

