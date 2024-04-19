package com.dhflour.dhflourdemo1.api.web;

import com.dhflour.dhflourdemo1.api.service.TestAPIService;
import com.dhflour.dhflourdemo1.core.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private TestAPIService testAPIService;

    @GetMapping("/api/v1/test")
    public ResponseEntity<?> test1() {
        // Use generics to specify the type of the keys and values in the map
        Map<String, Object> result = new HashMap<>();
        result.put("core", testService.test());
        result.put("api", testAPIService.test());
        return ResponseEntity.ok(result);
    }
}
