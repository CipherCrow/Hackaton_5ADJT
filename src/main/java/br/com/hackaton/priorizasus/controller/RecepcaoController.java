package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.AtualizarPacienteUseCase;
import br.com.hackaton.priorizasus.casosdeuso.BuscarPacienteUseCase;
import br.com.hackaton.priorizasus.casosdeuso.CadastrarPacienteUseCase;
import br.com.hackaton.priorizasus.casosdeuso.RegistrarChegadaUseCase;
import br.com.hackaton.priorizasus.dto.PacienteCadastradoDTO;
import br.com.hackaton.priorizasus.dto.PacienteParaCadastrarDTO;
import br.com.hackaton.priorizasus.dto.RegistroChegadaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recepcao")
@RequiredArgsConstructor
public class RecepcaoController {

    private final RegistrarChegadaUseCase registrarChegadaUseCase;
    private final CadastrarPacienteUseCase cadastrarPacienteUseCase;
    private final BuscarPacienteUseCase buscarPacienteUseCase;
    private final AtualizarPacienteUseCase atualizarPacienteUseCase;


    @PostMapping("/registrarChegada")
    public ResponseEntity<String> registrarChegada(@RequestBody @Valid RegistroChegadaDTO dto) {
        registrarChegadaUseCase.registrarChegada(dto.pacienteId(), dto.atendimentoAdministrativo());
        return ResponseEntity.ok("Paciente registrado com sucesso!");
    }

    @PostMapping("/cadastrarPaciente")
    public ResponseEntity<PacienteCadastradoDTO> cadastrarPaciente(@RequestBody @Valid PacienteParaCadastrarDTO dto) {
        var paciente = cadastrarPacienteUseCase.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(paciente);
    }

    @GetMapping("/buscarPaciente/{id}")
    public ResponseEntity<PacienteCadastradoDTO> buscarPacientePorCpf(@PathVariable Long id) {
        var paciente = buscarPacienteUseCase.buscarPorId(id);
        return ResponseEntity.ok(paciente);
    }

    @PutMapping("/atualizarPaciente/{id}")
    public ResponseEntity<String> atualizarPaciente(
            @PathVariable Long id,
            @RequestBody @Valid PacienteParaCadastrarDTO dto) {
        atualizarPacienteUseCase.atualizar(id, dto);
        return ResponseEntity.ok("Paciente atualizado com sucesso!");
    }

}

