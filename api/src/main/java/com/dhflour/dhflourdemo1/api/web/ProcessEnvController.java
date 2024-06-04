package com.dhflour.dhflourdemo1.api.web;

import com.dhflour.dhflourdemo1.api.service.TestAPIService;
import com.dhflour.dhflourdemo1.core.service.TestService;
import com.dhflour.dhflourdemo1.core.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProcessEnvController {

    @Value("${process.env}")
    private String processEnv;

    @Value("${management.endpoints.web.exposure.include}")
    private String prometheusEnv;

    @Autowired
    private TestService testService;

    @Autowired
    private TestAPIService testAPIService;

    @Autowired
    private MailService mailService;


    @GetMapping("/api/v1/env")
    public ResponseEntity<?> env() {
        // Use generics to specify the type of the keys and values in the map
        Map<String, Object> result = new HashMap<>();
        result.put("processEnv", processEnv);
        result.put("core", testService.test());
        result.put("api", testAPIService.test());
        result.put("mail", mailService.getText());
        result.put("prometheusEnv", prometheusEnv);
        return ResponseEntity.ok(result);
    }

    private static List<byte[]> memoryHog = new ArrayList<>();

    @GetMapping("/api/v1/memory-load")
    public ResponseEntity<?> memoryLoad() {
        memoryHog.add(new byte[10 * 1024 * 1024]); // 약 10MB 메모리 추가
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/v1/memory-load-size")
    public ResponseEntity<?> memoryLoadSize() {
        return ResponseEntity.ok(memoryHog.size());
    }

    @GetMapping("/api/v1/memory-load-clear")
    public ResponseEntity<?> memoryLoadClear() {
        memoryHog.clear();
        return ResponseEntity.ok().build();
    }
}
