package com.dhflour.dhflourdemo1.api.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

// https://spring.io/blog/2022/10/12/observability-with-spring-boot-3
@Slf4j
@RestController
public class InitWebFluxController {


    @GetMapping("/")
    public Mono<String> example() {
        try {
            // Your application logic here
            log.debug("Hello, OpenTelemetry");
            return Mono.just("Hello, OpenTelemetry!");
        } catch (Exception e) {
            throw e;
        }
    }
}