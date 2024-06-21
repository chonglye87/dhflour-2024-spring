package com.dhflour.dhflourdemo1.api.config.api;

import com.dhflour.dhflourdemo1.api.config.auth.CurrentUserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.data.web.ReactiveSortHandlerMethodArgumentResolver;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

/**
 * APIWebFluxConfigurer 클래스는 WebFlux 설정을 커스터마이징하기 위해 사용된다.
 * 이 클래스는 CORS 설정과 Argument Resolver 설정을 포함하여, API 호출 시 특정 규칙을 적용한다.
 */
@Configuration
@EnableWebFlux
public class APIWebFluxConfigurer implements WebFluxConfigurer {

    @Autowired
    private CurrentUserArgumentResolver currentUserArgumentResolver;

    /**
     * CORS 설정을 추가하는 메서드.
     * 특정 경로("/api/**")에 대해 허용할 출처, 메서드, 헤더 등을 설정한다.
     *
     * @param registry CorsRegistry 객체
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:8083",
                        "http://localhost:8082",
                        "http://43.201.69.193:3000",
                        "http://43.201.69.193"
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")  // 허용할 HTTP 메서드 지정, 기본적인 CRUD 작업에 사용되는 메서드 포함
                .allowedHeaders("*")  // 허용할 요청 헤더 지정, '*'는 모든 헤더를 허용함
                .allowCredentials(true);
    }

    /**
     * 커스텀 Argument Resolver를 추가하는 메서드.
     * 컨트롤러 메서드의 인자에 대한 해석을 커스터마이징 한다.
     *
     * @param configurer ArgumentResolverConfigurer 객체
     */
    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(currentUserArgumentResolver);  // 현재 사용자 정보를 해석하는 Argument Resolver 추가
        configurer.addCustomResolver(new ReactivePageableHandlerMethodArgumentResolver());  // 페이징 관련 Argument Resolver 추가
        configurer.addCustomResolver(new ReactiveSortHandlerMethodArgumentResolver());  // 정렬 관련 Argument Resolver 추가
    }
}

