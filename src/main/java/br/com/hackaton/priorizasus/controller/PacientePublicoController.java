package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.CadastrarPacientePublicamenteUseCase;
import br.com.hackaton.priorizasus.dto.PacienteCadastroPublicoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ações publicas", description = "Endpoints de ações que não precisam de permissão. Principalmente usado para o paciente se cadastrar por conta propria.")
@RestController
@RequestMapping("/publico/pacientes")
@RequiredArgsConstructor
public class PacientePublicoController {

    private final CadastrarPacientePublicamenteUseCase useCase;

    @Operation(
            summary = "Realiza o cadastro de um usuário",
            description = "Realiza o cadastro de um usuário junto com o paciente!"
    )
    @PostMapping("/cadastrar")
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid PacienteCadastroPublicoDTO dto) {
        useCase.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

