package com.dhflour.dhflourdemo1.api.web.auth;

import com.dhflour.dhflourdemo1.core.reactive.user.ReactiveUser;
import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import com.dhflour.dhflourdemo1.core.types.jwt.UserSampleBody;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
        ReactiveUser user = new ReactiveUser();
        user.setId(requestBody.getId());
        user.setUsername(requestBody.getUsername());
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return ResponseEntity.ok(jwtService.generateToken(new MyUserDetails(user, true, true, true, true, authorities)));
    }

    @Parameter(in = ParameterIn.HEADER, name = "Access-Token", description = "Access Token for authentication", required = true, schema = @Schema(implementation = String.class))
    @PostMapping("/verify-token")
    public ResponseEntity<?> accessToken(@RequestHeader Map<String, String> headers) {
        return ResponseEntity.ok(jwtService.verifyToken(headers.get("Access-Token")));
    }
}
