package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.dto.LoginRequestDTO;
import br.com.hackaton.priorizasus.dto.TokenResponseDTO;
import br.com.hackaton.priorizasus.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Login", description = "Endpoints para Realizar o Login de usuários no sistema.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @Operation(
            summary = "Realiza o login",
            description = "Realiza o login do usuário de acordo com o usuário e senha. Libera acesso de acordo com o perfil do usuário no banco."
    )
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        String token = authService.autenticar(dto);
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}
