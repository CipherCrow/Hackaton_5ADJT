package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.AtualizarStatusAtendimentoUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarFilaAtendimentoPorIdOuCpfUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarProximosAtendimentosUseCase;
import br.com.hackaton.priorizasus.casosdeuso.FinalizarAtendimentoUseCase;
import br.com.hackaton.priorizasus.dto.FilaAtendimentoDTO;
import br.com.hackaton.priorizasus.dto.FinalizarAtendimentoDTO;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.exception.EnumInvalidoException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filaAtendimento")
@RequiredArgsConstructor
public class AtendimentoController {

    private final BuscarProximosAtendimentosUseCase buscarProximosAtendimentosUseCase;
    private final BuscarFilaAtendimentoPorIdOuCpfUseCase buscarFilaAtendimentoPorIdOuCpfUseCase;
    private final AtualizarStatusAtendimentoUseCase atualizarStatusAtendimentoUseCase;
    private final FinalizarAtendimentoUseCase finalizarAtendimentoUseCase;

    @GetMapping("/aguardando")
    public ResponseEntity<List<FilaAtendimentoDTO>> buscarProximos() {
        List<FilaAtendimentoDTO> fila = buscarProximosAtendimentosUseCase.executar();
        return ResponseEntity.ok(fila);
    }

    @GetMapping("/buscarPorIdOuCpf/{idOuCpf}")
    public ResponseEntity<FilaAtendimentoDTO> buscarPorIdOuCpf(@PathVariable String idOuCpf) {
        FilaAtendimentoDTO fila = buscarFilaAtendimentoPorIdOuCpfUseCase.executar(idOuCpf);
        return ResponseEntity.ok(fila);
    }

    @PutMapping("/alterarStatus/{id}")
    public ResponseEntity<Void> alterarStatus(@PathVariable Long id, @RequestParam String novoStatus) {
        atualizarStatusAtendimentoUseCase.executar(id, converterParaStatusEnum(novoStatus));
        return ResponseEntity.noContent().build();
    }

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

