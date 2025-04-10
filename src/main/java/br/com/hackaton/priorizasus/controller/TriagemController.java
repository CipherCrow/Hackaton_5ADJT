package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.AlterarStatusFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarDezProximosFilaTriagemUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarPacienteFilaTriagemUseCase;
import br.com.hackaton.priorizasus.dto.FilaTriagemResponseDTO;
import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import br.com.hackaton.priorizasus.exception.EnumInvalidoException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/triagem")
@RequiredArgsConstructor
public class TriagemController {

    /*FILA DE TRIAGEM*/
    private final BuscarDezProximosFilaTriagemUseCase buscarProximosAguardando;
    private final BuscarPacienteFilaTriagemUseCase buscarPacienteFilaPorCpfUseCase;
    private final AlterarStatusFilaTriagemUseCase alterarStatusTriagemUseCase;

    @GetMapping("/filaTriagem/aguardandoTriagem")
    public ResponseEntity<List<FilaTriagemResponseDTO>> buscarProximosAguardando() {
        return ResponseEntity.ok(buscarProximosAguardando.buscarProximos());
    }

    @GetMapping("/filaTriagem/buscarPorIdOuCpf/{IdCpf}")
    public ResponseEntity<FilaTriagemResponseDTO> buscarPorIdOuCpf(@PathVariable String IdCpf) {
        return ResponseEntity.ok(buscarPacienteFilaPorCpfUseCase.buscar(IdCpf));
    }

    @PutMapping("/filaTriagem/alterarStatus/{id}")
    public ResponseEntity<String> alterarStatus(
            @PathVariable Long id,
            @RequestParam String novoStatus) {
        alterarStatusTriagemUseCase.alterarStatus(id, converterParaStatusEnum(novoStatus));
        return ResponseEntity.ok("Status atualizado com sucesso.");
    }

    private StatusTriagemEnum converterParaStatusEnum(String valor) {
        try {
            return StatusTriagemEnum.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new EnumInvalidoException("Valor inválido para o parâmetro: " + valor);
        }
    }

}

