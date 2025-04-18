package br.com.hackaton.priorizasus.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PriorizaSUS API")
                        .description("API para gerenciamento de triagem e atendimentos no SUS utilizando o Protocolo de Manchester.")
                        .version("1.0.0"));
    }
}
