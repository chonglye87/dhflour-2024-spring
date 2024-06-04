package com.dhflour.dhflourdemo1.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class InitWebFluxController {

    @Value("${spring.r2dbc.url}")
    private String url;

    @GetMapping("/")
    public Mono<String> helloWorld() {
        return Mono.just(url);
    }
}