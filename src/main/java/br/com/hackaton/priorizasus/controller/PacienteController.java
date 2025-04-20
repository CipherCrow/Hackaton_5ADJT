package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.BuscarHistoricoAtendimentosUseCase;
import br.com.hackaton.priorizasus.dto.HistoricoAtendimentoDTO;
import br.com.hackaton.priorizasus.infrastructure.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Ações do Paciente Logado",
        description = "Endpoint com as ações do paciente ao estar logado no sistema. A principio apenas para consultar as visitas anteriores.")
@RestController
@RequestMapping("/paciente")
@RequiredArgsConstructor
public class PacienteController {

    private final BuscarHistoricoAtendimentosUseCase buscarHistoricoAtendimentosUseCase;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "Busca histórico de visitas",
            description = "Realiza a busca de todo o histórico de visitas do paciente."
    )
    @GetMapping("/historico/buscarTodoHistorico")
    public ResponseEntity<List<HistoricoAtendimentoDTO>> buscarHistorico(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        String login = jwtUtil.getUsernameFromToken(token);

        List<HistoricoAtendimentoDTO> historico = buscarHistoricoAtendimentosUseCase.executar(login);
        return ResponseEntity.ok(historico);
    }
}

