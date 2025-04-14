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
                .authorizeHttpRequests(auth -> auth
                        // Libera o login
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        // Libera acesso público ao Swagger se quiser
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // TODO: libere os endpoints públicos aqui
                        .requestMatchers("/publico/**").permitAll()

                        // Tudo o resto precisa de autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

