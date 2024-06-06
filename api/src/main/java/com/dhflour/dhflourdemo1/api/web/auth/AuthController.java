package com.dhflour.dhflourdemo1.api.web.auth;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.service.user.UserAPIService;
import com.dhflour.dhflourdemo1.api.utils.AuthUtils;
import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.api.types.jwt.AuthenticationRequest;
import com.dhflour.dhflourdemo1.api.types.jwt.AuthenticationResponse;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import com.dhflour.dhflourdemo1.core.types.error.UnauthorizedException;
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

@Slf4j
@RequestMapping(value = "/api/v1/authenticate")
@RestController
public class AuthController {


    @Autowired
    private UserDetailsRepositoryReactiveAuthenticationManager authenticationManager;

    @Autowired
    private JWTSymmetricService jwtService;

    @Autowired
    private UserAPIService userAPIService;

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
                    RUser rUser = userAPIService.getUser(userDetails.getEmail()).block();
                    log.debug("reactiveUser : {}", rUser);
                    return ResponseEntity.ok(new AuthenticationResponse(jwt, rUser));
                }).onErrorResume(e -> {
                    log.error("Authentication error", e);
                    return Mono.error(new Exception("Incorrect username or password", e));
                });
    }

    @GetMapping(value = "/authenticated-info", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "인증된 사용자 정보 조회",
            description = "JWT를 통해 인증된 사용자 정보를 조회합니다.",
            operationId = "getAuthenticatedUserInfo",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "인증된 사용자 정보 조회 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RUser.class)))
    public Mono<ResponseEntity<RUser>> getAuthenticatedUserInfo(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> userAPIService.getUser(user.getEmail())
                        .map(userInfo -> ResponseEntity.ok().body(userInfo))
                        .switchIfEmpty(Mono.error(new UnauthorizedException("User not found"))));
    }

    @GetMapping(value = "/optional-info", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "옵션 사용자 정보 조회",
            description = "JWT를 통해 인증된 사용자 정보 또는 기본 정보를 조회합니다.",
            operationId = "getOptionalUserInfo",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RUser.class)))
    public Mono<ResponseEntity<RUser>> getOptionalUserInfo(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails) {
        return AuthUtils.optional(userDetails)
                .flatMap(user -> userAPIService.getUser(user.getEmail())
                        .map(userInfo -> ResponseEntity.ok().body(userInfo))
                        .switchIfEmpty(Mono.error(new UnauthorizedException("User not found"))))
                .defaultIfEmpty(ResponseEntity.ok().body(new RUser()));
    }
}
