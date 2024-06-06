package com.dhflour.dhflourdemo1.api.web.auth;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.service.user.UserAPIService;
import com.dhflour.dhflourdemo1.api.utils.AuthUtils;
import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.api.types.jwt.AuthenticationRequest;
import com.dhflour.dhflourdemo1.api.types.jwt.AuthenticationResponse;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import com.dhflour.dhflourdemo1.core.types.error.UnauthorizedException;
import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
import lombok.Setter;
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
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "[auth-1] JWT 발행",
            description = "JWT 발행합니다.",
            operationId = "access-token")
    @ApiResponse(responseCode = "200", description = "JWT 발행함",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthenticationResponse.class)))
    public Mono<ResponseEntity<AuthenticationResponse>> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()))
                .flatMap(authentication -> {
                    final ReactiveUserDetails userDetails = (ReactiveUserDetails) authentication.getPrincipal();
                    final String accessToken = jwtService.generateToken(userDetails.toMyUserDetails());
                    final String refreshToken = jwtService.generateRefreshToken(userDetails.toMyUserDetails());

                    return userAPIService.getUser(userDetails.getEmail())
                            .map(user -> ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken, user)))
                            .switchIfEmpty(Mono.error(new UnauthorizedException("User not found")));
                })
                .onErrorResume(e -> {
                    log.error("Authentication error", e);
                    return Mono.error(new Exception("Incorrect username or password", e));
                });
    }

    @PostMapping("/refresh-token")
    public Mono<ResponseEntity<AuthResponse>> refresh(@RequestBody RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();
        Claims claims = jwtService.verifyToken(refreshToken);

        if (jwtService.isTokenExpired(refreshToken)) {
            throw new UnauthorizedException("User not found");
        }

        return userAPIService.getUser(claims.getSubject())
                .flatMap(user -> {
                    MyUserDetails userDetails = MyUserDetails.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .build();

                    String newAccessToken = jwtService.generateToken(userDetails);

                    return Mono.just(ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken)));
                })
                .switchIfEmpty(Mono.error(new UnauthorizedException("User not found")));
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

    @Setter
    @Getter
    static class RefreshRequest {
        private String refreshToken;
    }

    @Setter
    @Getter
    static class AuthResponse {
        private String accessToken;
        private String refreshToken;

        public AuthResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        // Getters and setters
    }
}
