package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.AtualizarStatusAtendimentoUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarFilaAtendimentoPorIdOuCpfUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarProximosAtendimentosUseCase;
import br.com.hackaton.priorizasus.casosdeuso.FinalizarAtendimentoUseCase;
import br.com.hackaton.priorizasus.dto.FilaAtendimentoDTO;
import br.com.hackaton.priorizasus.dto.FinalizarAtendimentoDTO;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.exception.EnumInvalidoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Fila de Atendimento",
        description = "Endpoints para controle da fila de atendimento. Usado pelos profissionais.")
@RestController
@RequestMapping("/filaAtendimento")
@RequiredArgsConstructor
public class AtendimentoController {

    private final BuscarProximosAtendimentosUseCase buscarProximosAtendimentosUseCase;
    private final BuscarFilaAtendimentoPorIdOuCpfUseCase buscarFilaAtendimentoPorIdOuCpfUseCase;
    private final AtualizarStatusAtendimentoUseCase atualizarStatusAtendimentoUseCase;
    private final FinalizarAtendimentoUseCase finalizarAtendimentoUseCase;

    @Operation(
            summary = "Buscar proximos atendimentos",
            description = "Realiza a busca pelos proximos 10 pacientes que estão esperando atendimento. Utiliza como base o peso e prioridade."
    )
    @GetMapping("/aguardando")
    public ResponseEntity<List<FilaAtendimentoDTO>> buscarProximos() {
        List<FilaAtendimentoDTO> fila = buscarProximosAtendimentosUseCase.executar();
        return ResponseEntity.ok(fila);
    }

    @Operation(
            summary = "Busca atendimento pelo id ou cpf do paciente",
            description = "Realiza a busca pelo id do atendimento ou pelo cpf de paciente esperando atendimento."
    )
    @GetMapping("/buscarPorIdOuCpf/{idOuCpf}")
    public ResponseEntity<FilaAtendimentoDTO> buscarPorIdOuCpf(@PathVariable String idOuCpf) {
        FilaAtendimentoDTO fila = buscarFilaAtendimentoPorIdOuCpfUseCase.executar(idOuCpf);
        return ResponseEntity.ok(fila);
    }

    @Operation(
            summary = "Altera status de registro da fila",
            description = "Altera o status de um registro na fila."
    )
    @PutMapping("/alterarStatus/{id}")
    public ResponseEntity<Void> alterarStatus(@PathVariable Long id, @RequestParam String novoStatus) {
        atualizarStatusAtendimentoUseCase.executar(id, converterParaStatusEnum(novoStatus));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Inicia o atendimento",
            description = "Inicia o atendimento de um paciente na fila."
    )
    @PutMapping("/iniciarAtendimento/{id}")
    public ResponseEntity<Void> iniciarAtendimento(@PathVariable Long id) {
        atualizarStatusAtendimentoUseCase.executar(id, converterParaStatusEnum("EM_ATENDIMENTO"));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Finaliza atendimento",
            description = "Finaliza o atendimento de um paciente na fila. Cria um histórico para conseguir ser consultado."
    )
    @PutMapping("/finalizar")
    public ResponseEntity<String> finalizarAtendimento(@RequestBody @Valid FinalizarAtendimentoDTO dto) {
        finalizarAtendimentoUseCase.executar(dto);
        return ResponseEntity.ok("Atendimento Finalizado!");
    }

    private StatusAtendimentoEnum converterParaStatusEnum(String valor) {
        try {
            return StatusAtendimentoEnum.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new EnumInvalidoException("Valor inválido para o parâmetro: " + valor);
        }
    }

}

