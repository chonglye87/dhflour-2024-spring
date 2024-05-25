package com.dhflour.dhflourdemo1.api.web;

import com.dhflour.dhflourdemo1.api.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.core.types.jwt.UserSampleBody;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 대칭키용 JWT
@RestController
@RequestMapping("/api/symmetric/jwt")
public class JWTSymmetricController {

    @Autowired
    private JWTSymmetricService jwtService;

    @GetMapping("/create-key")
    public ResponseEntity<?> env() {
//        jwtService.createKey();
        jwtService.createSecretKey();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/access-token")
    public ResponseEntity<?> accessToken(@RequestBody UserSampleBody requestBody) {
        return ResponseEntity.ok(jwtService.generateToken(requestBody));
    }

    @Parameter(in = ParameterIn.HEADER, name = "Access-Token", description = "Access Token for authentication", required = true, schema = @Schema(implementation = String.class))
    @PostMapping("/verify-token")
    public ResponseEntity<?> accessToken(@RequestHeader Map<String, String> headers) {
        return ResponseEntity.ok(jwtService.verifyToken(headers.get("Access-Token")));
    }
}
