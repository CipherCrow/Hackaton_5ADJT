package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.BuscarHistoricoAtendimentosUseCase;
import br.com.hackaton.priorizasus.dto.HistoricoAtendimentoDTO;
import br.com.hackaton.priorizasus.infrastructure.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/paciente")
@RequiredArgsConstructor
public class PacienteController {

    private final BuscarHistoricoAtendimentosUseCase buscarHistoricoAtendimentosUseCase;
    private final JwtUtil jwtUtil;

    @GetMapping("/historico/buscarTodoHistorico")
    public ResponseEntity<List<HistoricoAtendimentoDTO>> buscarHistorico(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        String login = jwtUtil.getUsernameFromToken(token);

        List<HistoricoAtendimentoDTO> historico = buscarHistoricoAtendimentosUseCase.executar(login);
        return ResponseEntity.ok(historico);
    }
}

