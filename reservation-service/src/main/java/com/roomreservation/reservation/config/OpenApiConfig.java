package com.roomreservation.reservation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reservationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reservation Service API")
                        .description("API for managing room reservations")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Room Reservation Team")
                                .email("support@roomreservation.com"))
                        .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("/").description("Default Server URL")
                ));
    }
} 