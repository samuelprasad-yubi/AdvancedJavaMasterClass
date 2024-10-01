package com.jmca.easyauthapplication.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())  // Adding detailed API information
                .servers(apiServers())  // Defining multiple servers
                .components(new Components().addSecuritySchemes("Bearer Authentication", apiKeyScheme()))  // Defining security schemes
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));  // Adding security requirements
    }

    // Method to create API information (title, description, version, license, contact)
    private Info apiInfo() {
        return new Info()
                .title("My REST API")
                .description("Some custom description of the API. This includes comprehensive API documentation for developers.")
                .version("1.0.0")  // Versioning for the API
                .termsOfService("http://myapi.com/terms")  // Terms of service link
                .license(new License().name("API License").url("http://myapi.com/license"))  // License information
                .contact(new Contact()  // Contact information
                        .name("API Support")
                        .url("http://myapi.com/support")
                        .email("support@myapi.com"));
    }

    // Method to define different servers (development, production)
    private List<Server> apiServers() {
        return List.of(
                new Server().url("https://api.myapp.com/v1").description("Production server"),
                new Server().url("https://dev-api.myapp.com/v1").description("Development server")
        );
    }

    // Method to define the security scheme for JWT tokens
    private SecurityScheme apiKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT Bearer token for authentication");
    }
}
