package br.com.hackaton.priorizasus.controller;

import br.com.hackaton.priorizasus.dto.LoginRequestDTO;
import br.com.hackaton.priorizasus.dto.TokenResponseDTO;
import br.com.hackaton.priorizasus.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        String token = authService.autenticar(dto);
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}
