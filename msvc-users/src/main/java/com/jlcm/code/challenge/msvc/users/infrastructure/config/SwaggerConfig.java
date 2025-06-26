package com.jlcm.code.challenge.msvc.users.infrastructure.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.host:}")
    private String configUrl;

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("MSVC Users API")
                        .description("API for managing users in the MSVC Users microservice")
                        .version("1.0.0"))
                .servers(List.of(new Server().url(configUrl)));
    }
}
