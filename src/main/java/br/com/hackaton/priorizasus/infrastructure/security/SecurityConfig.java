package br.com.hackaton.priorizasus.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable()) // importante para travar ataques de csrf
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sem sessão

                //  APENAS PARA TESTE PARA DEIXAR VER O H2
                .headers(headers -> headers.frameOptions( frameOptions -> frameOptions.disable() ))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/publico/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // PACIENTE pode acessar apenas /paciente/**
                        .requestMatchers("/paciente/**").hasRole("PACIENTE")

                        // FUNCIONARIO pode acessar tudo EXCETO /paciente/**
                        .requestMatchers("/funcionario/**").hasAnyRole("FUNCIONARIO", "ADMINISTRADOR")
                        .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")

                        // Os administradores acessam tudo
                        .anyRequest().hasRole("ADMINISTRADOR")
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

