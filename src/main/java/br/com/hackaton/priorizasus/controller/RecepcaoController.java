package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.RegistrarChegadaUseCase;
import br.com.hackaton.priorizasus.dto.RegistroChegadaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ações da Recepção", description = "Endpoints para uso exclusivo da recepção.")
@RestController
@RequestMapping("/recepcao")
@RequiredArgsConstructor
public class RecepcaoController {

    private final RegistrarChegadaUseCase registrarChegadaUseCase;

    @Operation(
            summary = "Realiza pre-atendimento",
            description = "Realiza o pre-atendimento, adiciona paciente para fila de atendimento da triagem ou fila de atendimento normal."
    )
    @PostMapping("/registrarChegada")
    public ResponseEntity<String> registrarChegada(@RequestBody @Valid RegistroChegadaDTO dto) {
        registrarChegadaUseCase.registrarChegada(dto.pacienteId(), dto.atendimentoAdministrativo());
        return ResponseEntity.ok("Paciente registrado com sucesso!");
    }

}

