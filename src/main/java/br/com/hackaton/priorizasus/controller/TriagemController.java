package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.AlterarStatusFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarDezProximosFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarPacienteFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.RealizarTriagemUseCase;
import br.com.hackaton.priorizasus.dto.FilaTriagemResponseDTO;
import br.com.hackaton.priorizasus.dto.RealizarTriagemRequestDTO;
import br.com.hackaton.priorizasus.dto.TriagemResponseDTO;
import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import br.com.hackaton.priorizasus.exception.EnumInvalidoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Triagem", description = "Endpoints para controle e gerenciamento da fila e das triagens.")
@RestController
@RequestMapping("/triagem")
@RequiredArgsConstructor
public class TriagemController {

    /*FILA DE TRIAGEM*/
    private final BuscarDezProximosFilaTriagemUseCase buscarProximosAguardando;
    private final BuscarPacienteFilaTriagemUseCase buscarPacienteFilaPorCpfUseCase;
    private final AlterarStatusFilaTriagemUseCase alterarStatusTriagemUseCase;

    /*TRIAGEM*/
    private final RealizarTriagemUseCase realizarTriagemUseCase;

    @Operation(
            summary = "Realiza a busca das proximas pessoas aguardando triagem",
            description = "Realiza a busca das proximas 10 pessoas aguardando triagem."
    )
    @GetMapping("/filaTriagem/aguardandoTriagem")
    public ResponseEntity<List<FilaTriagemResponseDTO>> buscarProximosAguardando() {
        return ResponseEntity.ok(buscarProximosAguardando.buscarProximos());
    }

    @Operation(
            summary = "Realiza a busca por id ou cpf",
            description = "Realiza a busca na fila de traigem pelo id da fila de triagem ou pelo cpf aguardando."
    )
    @GetMapping("/filaTriagem/buscarPorIdOuCpf/{idCpf}")
    public ResponseEntity<FilaTriagemResponseDTO> buscarPorIdOuCpf(@PathVariable String idCpf) {
        return ResponseEntity.ok(buscarPacienteFilaPorCpfUseCase.buscar(idCpf));
    }

    @Operation(
            summary = "Altera Status da fila de triagem",
            description = "Altera o Status da fila de triagem."
    )
    @PutMapping("/filaTriagem/alterarStatus/{id}")
    public ResponseEntity<String> alterarStatus(
            @PathVariable Long id,
            @RequestParam String novoStatus) {
        alterarStatusTriagemUseCase.alterarStatus(id, converterParaStatusEnum(novoStatus));
        return ResponseEntity.ok("Status atualizado com sucesso.");
    }

    @Operation(
            summary = "Inicia uma Triagem",
            description = "Altera o Status da fila de triagem para em andamento."
    )
    @PutMapping("/filaTriagem/iniciarTriagem/{id}")
    public ResponseEntity<String> alterarStatus(
            @PathVariable Long id) {
        alterarStatusTriagemUseCase.alterarStatus(id, StatusTriagemEnum.EM_ANDAMENTO);
        return ResponseEntity.ok("Triagem iniciada com sucesso.");
    }


    @Operation(
            summary = "Finaliza a triagem",
            description = "Finaliza a triagem e passa o paciente para a fila de atendimento."
    )
    @PostMapping("/realizar")
    public ResponseEntity<TriagemResponseDTO> finalizarTriagem(@RequestBody @Valid RealizarTriagemRequestDTO dto) {
        TriagemResponseDTO response = realizarTriagemUseCase.realizarTriagem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    private StatusTriagemEnum converterParaStatusEnum(String valor) {
        try {
            return StatusTriagemEnum.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new EnumInvalidoException("Valor inválido para o parâmetro: " + valor);
        }
    }

}

