package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.FilaTriagemResponseDTO;
import br.com.hackaton.priorizasus.entities.FilaTriagem;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import br.com.hackaton.priorizasus.repository.FilaTriagemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuscarDezProximosFilaTriagemUseCaseTest {

    @Mock
    private FilaTriagemRepository filaTriagemRepository;

    private BuscarDezProximosFilaTriagemUseCase buscarDezProximosFilaTriagemUseCase;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        buscarDezProximosFilaTriagemUseCase = new BuscarDezProximosFilaTriagemUseCase(filaTriagemRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveRetornarProximos10PacientesAguardando() {
        // Arrange
        List<FilaTriagem> lista = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Paciente paciente = new Paciente();
            paciente.setNome("Paciente " + i);
            paciente.setCpf("1234567890" + i);

            FilaTriagem fila = new FilaTriagem();
            fila.setId((long) i);
            fila.setPaciente(paciente);
            fila.setStatusTriagem(StatusTriagemEnum.AGUARDANDO);
            fila.setHorarioEntrada(LocalDateTime.now());

            lista.add(fila);
        }

        when(filaTriagemRepository.findTop10ByStatusTriagemOrderByHorarioEntradaAsc(StatusTriagemEnum.AGUARDANDO))
                .thenReturn(lista);

        // Act
        List<FilaTriagemResponseDTO> resultado = buscarDezProximosFilaTriagemUseCase.buscarProximos();

        // Assert
        assertNotNull(resultado);
        assertEquals(10, resultado.size());
        assertEquals("Paciente 1", resultado.get(0).nomePaciente());
        verify(filaTriagemRepository).findTop10ByStatusTriagemOrderByHorarioEntradaAsc(StatusTriagemEnum.AGUARDANDO);
    }
}


