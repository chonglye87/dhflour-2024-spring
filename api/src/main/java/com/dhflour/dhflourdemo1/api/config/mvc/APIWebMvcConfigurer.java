//package com.dhflour.dhflourdemo1.api.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class APIWebMvcConfigurer implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // CORS 정책을 설정하는 메서드
//        registry.addMapping("/api/**")  // CORS를 적용할 URL 패턴 지정, '/api/**'는 '/api/'로 시작하는 모든 경로 포함
//                .allowedOrigins("http://example.com", "http://localhost:3000", "http://localhost:8083", "http://localhost:8082")  // 허용할 원점(도메인) 지정, 여기서는 example.com과 localhost를 허용
//                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")  // 허용할 HTTP 메서드 지정, 기본적인 CRUD 작업에 사용되는 메서드 포함
//                .allowedHeaders("*")  // 허용할 요청 헤더 지정, '*'는 모든 헤더를 허용함
//                .allowCredentials(true);  // 자격증명(쿠키, HTTP 인증, 클라이언트 SSL 인증 등) 포함한 요청의 허용 여부 설정, true로 설정
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // '/static/**' 경로로 들어오는 요청을 처리하기 위한 리소스 핸들러
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/static/");
//
//        // '/public/**' 경로로 들어오는 요청을 처리하기 위한 리소스 핸들러
//        registry.addResourceHandler("/public/**")
//                .addResourceLocations("classpath:/public/");
//    }
//
//
//}
