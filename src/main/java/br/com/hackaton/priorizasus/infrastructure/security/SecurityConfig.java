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
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))

                .authorizeHttpRequests(auth -> auth
                        // Swagger/OpenAPI
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs", "/v3/api-docs/**").permitAll()

                        // públicos
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/publico/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // paciência: só pacientes
                        .requestMatchers("/paciente/**").hasAnyAuthority("PACIENTE", "ADMINISTRADOR")

                        // funcionários e admins (mas não /paciente/**)
                        .requestMatchers("/filaAtendimento/**").hasAnyAuthority("FUNCIONARIO", "ADMINISTRADOR")
                        .requestMatchers("/recepcao/**").hasAnyAuthority("FUNCIONARIO", "ADMINISTRADOR")
                        .requestMatchers("/administrativo/**").hasAnyAuthority("FUNCIONARIO", "ADMINISTRADOR")
                        .requestMatchers("/triagem/**").hasAnyAuthority("FUNCIONARIO", "ADMINISTRADOR")

                        // todo o resto só admin
                        .anyRequest().hasAuthority("ADMINISTRADOR")
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}

