package com.dhflour.dhflourdemo1.api.config.api;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${springdoc.info.title}")
    private String title;

    @Value("${springdoc.info.description}")
    private String description;

    @Value("${springdoc.info.version}")
    private String version;

    @Bean
    public OpenAPI springShopOpenAPI() {
        // API 정보 객체를 생성하고 타이틀, 설명, 버전을 설정합니다.
        final Info info = new Info().title(title).description(description).version(version);

        // 보안 스키마 객체를 생성하여 Bearer 형식의 JWT 인증을 설정합니다.
        final SecurityScheme securityScheme = new SecurityScheme()
                .name("bearerAuth") // 보안 스키마의 이름을 설정합니다.
                .type(SecurityScheme.Type.HTTP) // 보안 스키마의 타입을 HTTP로 설정합니다.
                .scheme("bearer") // HTTP 스키마의 유형을 Bearer로 설정합니다.
                .bearerFormat("JWT"); // Bearer 토큰의 형식을 JWT로 설정합니다.

        // 컴포넌트 객체를 생성하고 앞서 정의한 보안 스키마를 추가합니다.
        final Components components = new Components().addSecuritySchemes("bearerAuth", securityScheme);
        return new OpenAPI().info(info).components(components);
    }
}
