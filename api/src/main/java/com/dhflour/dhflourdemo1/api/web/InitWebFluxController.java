package com.dhflour.dhflourdemo1.api.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class InitWebFluxController {

    @GetMapping("/")
    public Mono<String> helloWorld() {
        return Mono.just("Hello World!");
    }
}