package br.com.hackaton.priorizasus.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String CONTROLLERS = "br.com.hackaton.priorizasus.controller";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .description("Informe o token JWT obtido no /auth/login")))
                .info(new Info()
                        .title("PriorizaSUS API")
                        .description("API para gerenciamento inteligente de filas no SUS.")
                        .version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi administrativoGroup() {
        return GroupedOpenApi.builder()
                .group("Administrativo")
                .displayName("Endpoints do Administrativo")
                .packagesToScan(CONTROLLERS)   // ajuste para o pacote certo
                .pathsToMatch("/administrativo/**")
                .build();
    }

    @Bean
    public GroupedOpenApi atendimentoGroup() {
        return GroupedOpenApi.builder()
                .group("Atendimento")
                .displayName("Endpoints da Fila de Atendimento")
                .packagesToScan(CONTROLLERS)   // ajuste para o pacote certo
                .pathsToMatch("/filaAtendimento/**")
                .build();
    }

    @Bean
    public GroupedOpenApi loginGroup() {
        return GroupedOpenApi.builder()
                .group("Login")
                .displayName("Realizar Login")
                .packagesToScan(CONTROLLERS)
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi pacienteGroup() {
        return GroupedOpenApi.builder()
                .group("Paciente")
                .displayName("Endpoints do paciente Logado")
                .packagesToScan(CONTROLLERS)
                .pathsToMatch("/paciente/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicosGroup() {
        return GroupedOpenApi.builder()
                .group("Publicos")
                .displayName("Endpoints de acesso publico")
                .packagesToScan(CONTROLLERS)
                .pathsToMatch("/publico/**")
                .build();
    }

    @Bean
    public GroupedOpenApi recepcaoGroup() {
        return GroupedOpenApi.builder()
                .group("Recepcao")
                .displayName("Endpoints da recepção")
                .packagesToScan(CONTROLLERS)
                .pathsToMatch("/recepcao/**")
                .build();
    }

    @Bean
    public GroupedOpenApi triagemGroup() {
        return GroupedOpenApi.builder()
                .group("Triagem")
                .displayName("Endpoints do processo de Triagem")
                .packagesToScan(CONTROLLERS)
                .pathsToMatch("/triagem/**")
                .build();
    }
}
