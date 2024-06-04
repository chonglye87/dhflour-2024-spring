package com.dhflour.dhflourdemo1.api.web.auth;

import com.dhflour.dhflourdemo1.api.repository.user.ReactiveUserRepository;
import com.dhflour.dhflourdemo1.api.service.userdetail.MyReactiveUserDetailsService;
import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.api.types.jwt.AuthenticationRequest;
import com.dhflour.dhflourdemo1.api.types.jwt.AuthenticationResponse;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RequestMapping(value = "/api/v1/authenticate")
@RestController
public class AuthController {


    @Autowired
    private UserDetailsRepositoryReactiveAuthenticationManager authenticationManager;

    @Autowired
    private JWTSymmetricService jwtService;

    @Autowired
    private ReactiveUserRepository reactiveUserRepository;

    @Autowired
    private MyReactiveUserDetailsService userDetailsService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "[auth-1] JWT 발행",
            description = "JWT 발행합니다.",
            operationId = "authenticate")
    @ApiResponse(responseCode = "200", description = "JWT 발행함",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthenticationResponse.class)))
    public Mono<ResponseEntity<AuthenticationResponse>> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()))
                .map(authentication -> {
                    final ReactiveUserDetails userDetails = (ReactiveUserDetails) authentication.getPrincipal();
                    final String jwt = jwtService.generateToken(userDetails.toMyUserDetails());
                    return ResponseEntity.ok(new AuthenticationResponse(jwt, reactiveUserRepository.findOneByEmail(userDetails.getEmail()).block()));
                }).onErrorResume(e -> {
                    log.error("Authentication error", e);
                    return Mono.error(new Exception("Incorrect username or password", e));
                });
    }

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "인증된 사용자 정보 조회",
            description = "JWT를 통해 인증된 사용자 정보를 조회합니다.",
            operationId = "getAuthenticatedUserInfo",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "인증된 사용자 정보 조회 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ReactiveUserDetails.class)))
    public Mono<ResponseEntity<Map<String, Object>>> getAuthenticatedUserInfo(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails) {
        return userDetails.map(user -> {
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            return ResponseEntity.ok(response);
        });
    }
}
