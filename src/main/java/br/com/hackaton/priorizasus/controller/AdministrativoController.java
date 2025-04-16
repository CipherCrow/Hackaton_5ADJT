package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.*;
import br.com.hackaton.priorizasus.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/administrativo")
@RequiredArgsConstructor
public class AdministrativoController {

    //Pacientes
    private final CadastrarPacienteUseCase cadastrarPaciente;
    private final BuscarPacienteUseCase buscarPacienteUseCase;
    private final AtualizarPacienteUseCase atualizarPacienteUseCase;

    //Sintomas
    private final CadastrarSintomaUseCase cadastrarSintoma;
    private final VisualizarTodosOsSintomasUseCase visualizarTodosOsSintomasUseCase;
    private final VisualizarSintomaPorIdUseCase  visualizarSintomaPorIdUseCase;
    private final AtualizarSintomaUseCase atualizarSintomaUseCase;

    //Profissionais
    private final CadastrarProfissionalUseCase cadastrarProfissional;
    private final BuscarTodosProfissionaisUseCase buscarTodosProfissionaisUseCase;
    private final BuscarProfissionalPorIdUseCase buscarProfissionalPorIdUseCase;
    private final AtualizarProfissionalUseCase atualizarProfissionalUseCase;

    //Usuarios
    private final CriarUsuarioPacienteUseCase criarUsuarioPacienteUseCase;

    // Paciente
    @PostMapping("/cadastrarPaciente")
    public ResponseEntity<PacienteCadastradoDTO> cadastrarPaciente(@RequestBody @Valid PacienteParaCadastrarDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrarPaciente.cadastrar(dto));
    }

    @GetMapping("/buscarPacienteId/{id}")
    public ResponseEntity<PacienteCadastradoDTO> buscarPacientePorId(@PathVariable Long id) {
        var paciente = buscarPacienteUseCase.buscarPorId(id);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/buscarPacienteCpf/{cpf}")
    public ResponseEntity<PacienteCadastradoDTO> buscarPacientePorCpf(@PathVariable String cpf) {
        var paciente = buscarPacienteUseCase.buscarPorCpf(cpf);
        return ResponseEntity.ok(paciente);
    }

    @PutMapping("/atualizarPaciente/{id}")
    public ResponseEntity<String> atualizarPaciente(
            @PathVariable Long id,
            @RequestBody @Valid PacienteParaCadastrarDTO dto) {
        atualizarPacienteUseCase.atualizar(id, dto);
        return ResponseEntity.ok("Paciente atualizado com sucesso!");
    }

    // Sintoma
    @PostMapping("/cadastrarSintomas")
    public ResponseEntity<SintomaDTO> cadastrarSintoma(@RequestBody @Valid SintomaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrarSintoma.cadastrarSintoma(dto));
    }

    @GetMapping("/Buscarsintomas")
    public ResponseEntity<List<SintomaDTO>> visualizarTodosSintomas() {
        return ResponseEntity.ok(visualizarTodosOsSintomasUseCase.buscarTodosOsSintomas());
    }

    @GetMapping("/BuscarSintoma/{id}")
    public ResponseEntity<SintomaDTO> visualizarSintomaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(visualizarSintomaPorIdUseCase.buscarSintoma(id));
    }

    @PutMapping("/atualizarSintoma/{id}")
    public ResponseEntity<String> atualizarPaciente(
            @PathVariable Long id,
            @RequestBody @Valid SintomaRequestDTO dto) {
        atualizarSintomaUseCase.atualizar(id, dto);
        return ResponseEntity.ok("Sintoma atualizado com sucesso!");
    }


    // Profissional de Saúde
    @PostMapping("/cadastrarProfissional")
    public ResponseEntity<ProfissionalSaudeDTO> cadastrarProfissional(@RequestBody @Valid ProfissionalSaudeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrarProfissional.cadastrar(dto));
    }

    @GetMapping("/buscarTodosProfissionais")
    public ResponseEntity<List<ProfissionalSaudeDTO>> buscarTodos() {
        return ResponseEntity.ok(buscarTodosProfissionaisUseCase.buscarTodos());
    }

    @GetMapping("/buscarProfissional/{id}")
    public ResponseEntity<ProfissionalSaudeDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(buscarProfissionalPorIdUseCase.buscarPorId(id));
    }

    @PutMapping("/atualizarProfissional/{id}")
    public ResponseEntity<String> atualizarProfissional(
            @PathVariable Long id,
            @RequestBody @Valid ProfissionalSaudeRequestDTO dto) {
        atualizarProfissionalUseCase.atualizar(id, dto);
        return ResponseEntity.ok("Profissional atualizado com sucesso!");
    }

    //Usuarios
    @PostMapping("/usuarios/criarUsuario")
    public ResponseEntity<String> criarUsuarioParaPaciente(@RequestBody @Valid CriarUsuarioPacienteDTO dto) {
        criarUsuarioPacienteUseCase.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario criado para o paciente com sucesso!");
    }
}
