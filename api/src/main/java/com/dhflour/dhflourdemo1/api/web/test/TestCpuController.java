package com.dhflour.dhflourdemo1.api.web.test;

import com.dhflour.dhflourdemo1.api.service.test.CpuIntensiveTask;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@Tag(name = "테스트 API")
@RequestMapping("/api/v1/test")
@RestController
public class TestCpuController {


    @GetMapping("/cpu")
    public Mono<String> cpu() {
        for(int i = 0; i < 100; i++) {
            CpuIntensiveTask.performCpuIntensiveTask();
            log.debug("i : {}", i);
        }
        return Mono.just("CPU intensive task performed");
    }
}
