package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.casosdeuso.*;
import br.com.hackaton.priorizasus.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ações Administrativas"
        , description = "Endpoints para cadastro, consulta e atualização dos registros administrativos: Usuários, Sintomas, Profissionais e usuários.")
@RestController
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
    @Operation(
            summary = "Realiza o cadastro do paciente",
            description = "Cadastra o paciente ainda sem o usuário."
    )
    @PostMapping("/cadastrarPaciente")
    public ResponseEntity<PacienteCadastradoDTO> cadastrarPaciente(@RequestBody @Valid PacienteParaCadastrarDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrarPaciente.cadastrar(dto));
    }

    @Operation(
            summary = "Realiza a busca de um paciente já cadastrado pelo ID",
            description = "Busca paciente já cadastrado através do ID"
    )
    @GetMapping("/buscarPacienteId/{id}")
    public ResponseEntity<PacienteCadastradoDTO> buscarPacientePorId(@PathVariable Long id) {
        var paciente = buscarPacienteUseCase.buscarPorId(id);
        return ResponseEntity.ok(paciente);
    }

    @Operation(
            summary = "Realiza a busca de um paciente já cadastrado pelo CPF",
            description = "Busca paciente já cadastrado através do CPF"
    )
    @GetMapping("/buscarPacienteCpf/{cpf}")
    public ResponseEntity<PacienteCadastradoDTO> buscarPacientePorCpf(@PathVariable String cpf) {
        var paciente = buscarPacienteUseCase.buscarPorCpf(cpf);
        return ResponseEntity.ok(paciente);
    }

    @Operation(
            summary = "Atualiza os dados de algum paciente já através do ID",
            description = "Realiza a atualização dos dados do paciente através do ID. Recomenda-se encontrar o paciente antes com o CPF."
    )
    @PutMapping("/atualizarPaciente/{id}")
    public ResponseEntity<String> atualizarPaciente(
            @PathVariable Long id,
            @RequestBody @Valid PacienteParaCadastrarDTO dto) {
        atualizarPacienteUseCase.atualizar(id, dto);
        return ResponseEntity.ok("Paciente atualizado com sucesso!");
    }

    // Sintoma
    @Operation(
            summary = "Realiza o cadastro do sintoma",
            description = "Cadastrar sintomas para serem usados nas triagens."
    )
    @PostMapping("/cadastrarSintomas")
    public ResponseEntity<SintomaDTO> cadastrarSintoma(@RequestBody @Valid SintomaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrarSintoma.cadastrarSintoma(dto));
    }

    @Operation(
            summary = "Realiza a busca dos sintomas",
            description = "Usado durante a triagem para encontrar os sintomas que já foram cadastrados anteriormente ou notar a necessidade de cadastrar um novo."
    )
    @GetMapping("/Buscarsintomas")
    public ResponseEntity<List<SintomaDTO>> visualizarTodosSintomas() {
        return ResponseEntity.ok(visualizarTodosOsSintomasUseCase.buscarTodosOsSintomas());
    }

    @Operation(
            summary = "Realiza a busca de um sintoma especifico",
            description = "Usado durante a triagem para encontra um sintoma em especifico"
    )
    @GetMapping("/BuscarSintoma/{id}")
    public ResponseEntity<SintomaDTO> visualizarSintomaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(visualizarSintomaPorIdUseCase.buscarSintoma(id));
    }

    @Operation(
            summary = "Realiza a atualização do sintoma",
            description = "Usado durante a triagem para encontrar um sintoma especifico."
    )
    @PutMapping("/atualizarSintoma/{id}")
    public ResponseEntity<String> atualizarPaciente(
            @PathVariable Long id,
            @RequestBody @Valid SintomaRequestDTO dto) {
        atualizarSintomaUseCase.atualizar(id, dto);
        return ResponseEntity.ok("Sintoma atualizado com sucesso!");
    }


    // Profissional de Saúde
    @Operation(
            summary = "Realiza o cadastro dos novos profissionais de saúde sem o usuário.",
            description = "Realiza o cadastro do profissional de saúde sem o usuário."
    )
    @PostMapping("/cadastrarProfissional")
    public ResponseEntity<ProfissionalSaudeDTO> cadastrarProfissional(@RequestBody @Valid ProfissionalSaudeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrarProfissional.cadastrar(dto));
    }

    @Operation(
            summary = "Realiza a busca de todos os novos profissionais já cadastrados",
            description = "Realiza a busca de todos os novos profissionais já cadastrados."
    )
    @GetMapping("/buscarTodosProfissionais")
    public ResponseEntity<List<ProfissionalSaudeDTO>> buscarTodos() {
        return ResponseEntity.ok(buscarTodosProfissionaisUseCase.buscarTodos());
    }

    @Operation(
            summary = "Realiza a busca pelo id do profissional",
            description = "Realiza a busca pelo id do profissional ja cadastrado."
    )
    @GetMapping("/buscarProfissional/{id}")
    public ResponseEntity<ProfissionalSaudeDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(buscarProfissionalPorIdUseCase.buscarPorId(id));
    }

    @Operation(
            summary = "Atualizar profissional",
            description = "Realiza a atualização cadastral de um profissional ja cadastrado."
    )
    @PutMapping("/atualizarProfissional/{id}")
    public ResponseEntity<String> atualizarProfissional(
            @PathVariable Long id,
            @RequestBody @Valid ProfissionalSaudeRequestDTO dto) {
        atualizarProfissionalUseCase.atualizar(id, dto);
        return ResponseEntity.ok("Profissional atualizado com sucesso!");
    }

    @Operation(
            summary = "Criar usuário do paciente",
            description = "Realiza a criação de um usuário para o paciente já criado anteriormente pelo atendimento."
    )
    //Usuarios
    @PostMapping("/usuarios/criarUsuario")
    public ResponseEntity<String> criarUsuarioParaPaciente(@RequestBody @Valid CriarUsuarioPacienteDTO dto) {
        criarUsuarioPacienteUseCase.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario criado para o paciente com sucesso!");
    }
}
