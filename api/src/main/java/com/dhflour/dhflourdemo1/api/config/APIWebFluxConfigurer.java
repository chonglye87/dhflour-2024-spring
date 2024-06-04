package com.dhflour.dhflourdemo1.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class APIWebFluxConfigurer implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://example.com", "http://localhost:3000", "http://localhost:8083", "http://localhost:8082")  // 허용할 원점(도메인) 지정, 여기서는 example.com과 localhost를 허용
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")  // 허용할 HTTP 메서드 지정, 기본적인 CRUD 작업에 사용되는 메서드 포함
                .allowedHeaders("*")  // 허용할 요청 헤더 지정, '*'는 모든 헤더를 허용함
                .allowCredentials(true);
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        WebFluxConfigurer.super.addResourceHandlers(registry);
//
//        // '/static/**' 경로로 들어오는 요청을 처리하기 위한 리소스 핸들러
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/static/");
//
//        // '/public/**' 경로로 들어오는 요청을 처리하기 위한 리소스 핸들러
//        registry.addResourceHandler("/public/**")
//                .addResourceLocations("classpath:/public/");
//    }
}
