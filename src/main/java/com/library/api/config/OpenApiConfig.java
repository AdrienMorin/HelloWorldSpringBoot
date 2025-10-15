package com.library.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for OpenAPI/Swagger documentation.
 *
 * Provides comprehensive API documentation accessible through Swagger UI,
 * making it easier for developers to understand and test the API.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures the OpenAPI documentation.
     *
     * @return configured OpenAPI object
     */
    @Bean
    public OpenAPI libraryApiOpenAPI() {
        Server devServer = new Server()
                .url("http://localhost:8080")
                .description("Development server");

        Contact contact = new Contact()
                .name("Library API Support")
                .email("support@library-api.com")
                .url("https://library-api.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Library Management API")
                .version("1.0.0")
                .description("RESTful API for managing a book library system. " +
                        "This API provides endpoints for creating, reading, updating, " +
                        "and deleting books, as well as searching capabilities.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}
