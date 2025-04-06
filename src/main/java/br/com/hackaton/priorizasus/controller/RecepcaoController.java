package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.RegistrarChegadaUseCase;
import br.com.hackaton.priorizasus.dto.RegistroChegadaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recepcao")
@RequiredArgsConstructor
public class RecepcaoController {

    private final RegistrarChegadaUseCase registrarChegadaUseCase;

    @PostMapping("/registrarChegada")
    public ResponseEntity<String> registrarChegada(@RequestBody @Valid RegistroChegadaDTO dto) {
        registrarChegadaUseCase.registrarChegada(dto.pacienteId(), dto.atendimentoAdministrativo());
        return ResponseEntity.ok("Paciente registrado com sucesso!");
    }

}

