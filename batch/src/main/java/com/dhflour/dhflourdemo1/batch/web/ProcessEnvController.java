package com.dhflour.dhflourdemo1.batch;

import com.dhflour.dhflourdemo1.core.service.TestService;
import com.dhflour.dhflourdemo1.core.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
    private MailService mailService;

    @GetMapping("/api/v1/env")
    public ResponseEntity<?> env() {
        // Use generics to specify the type of the keys and values in the map
        Map<String, Object> result = new HashMap<>();
        result.put("processEnv", processEnv);
        result.put("core", testService.test());
        result.put("mail", mailService.getText());
        result.put("prometheusEnv", prometheusEnv);
        return ResponseEntity.ok(result);
    }
}
