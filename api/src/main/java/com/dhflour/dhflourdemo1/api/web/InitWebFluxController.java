package com.dhflour.dhflourdemo1.api.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class InitWebFluxController {

    @Value("${logging.file.name}")
    private String url;

    @GetMapping("/")
    public Mono<String> helloWorld() {
        return Mono.just(url);
    }
}