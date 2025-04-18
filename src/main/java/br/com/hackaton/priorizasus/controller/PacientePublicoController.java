package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.CadastrarPacientePublicamenteUseCase;
import br.com.hackaton.priorizasus.dto.PacienteCadastroPublicoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publico/pacientes")
@RequiredArgsConstructor
public class PacientePublicoController {

    private final CadastrarPacientePublicamenteUseCase useCase;

    @PostMapping("/cadastrar")
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid PacienteCadastroPublicoDTO dto) {
        useCase.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

