package com.pcfinal.apiapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Productos")
                        .description("API REST para la gestión de productos con autenticación JWT y autorización por roles")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tu Nombre")
                                .email("tu.email@ejemplo.com")
                                .url("https://tu-website.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Introduce el token JWT en el formato: Bearer {token}")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
