package com.dhflour.dhflourdemo1.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API 문서")
                        .description("대한제분 API, powered by Spring Boot 3")
                        .version("v1.0.0"));
    }
}
