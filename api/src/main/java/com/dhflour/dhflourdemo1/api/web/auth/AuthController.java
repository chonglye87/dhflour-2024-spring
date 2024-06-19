package com.dhflour.dhflourdemo1.api.web.auth;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.service.user.UserAPIService;
import com.dhflour.dhflourdemo1.api.types.jwt.AuthenticationRequest;
import com.dhflour.dhflourdemo1.api.types.jwt.AuthenticationResponse;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import com.dhflour.dhflourdemo1.api.utils.AuthUtils;
import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.core.types.error.BadRequestException;
import com.dhflour.dhflourdemo1.core.types.error.UnauthorizedException;
import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import com.dhflour.dhflourdemo1.core.types.jwt.RefreshTokenRequest;
import com.dhflour.dhflourdemo1.core.types.jwt.RefreshTokenResponse;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Slf4j
@RequestMapping(value = "/api/v1/authenticate")
@Tag(name = "인증 API", description = "사용자 인증 및 토큰 관리를 위한 API")
@RestController
public class AuthController {

    final private static UnauthorizedException ERROR_USER_NOT_FOUND = new UnauthorizedException("User not found");
    final private static UnauthorizedException ERROR_EXPIRED_REFRESH_TOKEN = new UnauthorizedException("Expired refresh token");

    @Autowired
    private UserDetailsRepositoryReactiveAuthenticationManager authenticationManager;

    @Autowired
    private JWTSymmetricService jwtService;

    @Autowired
    private UserAPIService userAPIService;

    @PostMapping(value = "/access-token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "[auth-1] JWT Access Token 발행", description = "JWT Access Token 발행합니다.", operationId = "authAccessToken")
    @ApiResponse(responseCode = "200", description = "JWT Access Token 발행함", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthenticationResponse.class)))
    public Mono<?> createAuthenticationToken(@RequestBody Mono<AuthenticationRequest> monoBody, ServerWebExchange exchange) {
        return monoBody
                .filter(request -> StringUtils.isNotEmpty(request.getEmail()))
                .switchIfEmpty(Mono.error(new BadRequestException("Email is empty")))
                .filter(request -> StringUtils.isNotEmpty(request.getPassword()))
                .switchIfEmpty(Mono.error(new BadRequestException("Password is empty")))
                .flatMap(request -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()))).flatMap(authentication -> {
                    try {
                        log.debug("authentication : {}", authentication);
                        final ReactiveUserDetails userDetails = (ReactiveUserDetails) authentication.getPrincipal();
                        final MyUserDetails myUserDetails = userDetails.toMyUserDetails();
                        final String accessToken = jwtService.generateToken(myUserDetails);
                        final String refreshToken = jwtService.generateRefreshToken(myUserDetails);
                        userAPIService.saveRefreshToken(refreshToken, exchange); // TODO 저장토큰 비교 및 검증 & 문제 없으면 기존 토큰 삭제
                        return userAPIService.getActiveUser(myUserDetails.getEmail()).map(user -> new AuthenticationResponse(accessToken, refreshToken, user)).switchIfEmpty(Mono.error(ERROR_USER_NOT_FOUND));
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return Mono.error(ERROR_USER_NOT_FOUND);
                    }
                }).switchIfEmpty(Mono.error(new BadCredentialsException("로그인을 실패하였습니다. 다시 시도해주세요.")));
    }

    @PostMapping(value = "/refresh-token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "[auth-2] JWT Refresh Token 발행", description = "JWT Refresh Token 발행합니다.", operationId = "authRefreshToken")
    @ApiResponse(responseCode = "200", description = "JWT Refresh Token 발행함", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthenticationResponse.class)))
    public Mono<?> refresh(@RequestBody Mono<RefreshTokenRequest> monoBody, ServerWebExchange exchange) {
        return monoBody
                .filter(body -> StringUtils.isNotEmpty(body.getRefreshToken())).switchIfEmpty(Mono.error(new BadRequestException("Refresh token is empty")))
                .filter(body -> {
                    final Claims claims = jwtService.verifyToken(body.getRefreshToken());
                    return !jwtService.isTokenExpired(body.getRefreshToken()) && NumberUtils.isCreatable(claims.getId());
                }).switchIfEmpty(Mono.error(ERROR_EXPIRED_REFRESH_TOKEN))
                .flatMap(body -> {
                    final String refreshToken = body.getRefreshToken();
                    final Claims claims = jwtService.verifyToken(refreshToken);
                    return userAPIService.varifyRefreshToken(refreshToken, exchange)
                            .filter(Boolean::booleanValue)
                            .switchIfEmpty(Mono.error(ERROR_EXPIRED_REFRESH_TOKEN))
                            .then(userAPIService.getActiveUser(Long.parseLong(claims.getId())));
                }).switchIfEmpty(Mono.error(ERROR_USER_NOT_FOUND))
                .flatMap(rUser -> {
                    try {
                        final MyUserDetails myUserDetails = MyUserDetails.builder().id(rUser.getId()).email(rUser.getEmail()).build();
                        final String newAccessToken = jwtService.generateToken(myUserDetails);
                        final String newRefreshToken = jwtService.generateRefreshToken(myUserDetails);
                        userAPIService.saveRefreshToken(newRefreshToken, exchange); // TODO 저장토큰 비교 및 검증 & 문제 없으면 기존 토큰 삭제
                        return Mono.just(RefreshTokenResponse.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build());
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return Mono.error(ERROR_USER_NOT_FOUND);
                    }
                }).switchIfEmpty(Mono.error(ERROR_USER_NOT_FOUND));
    }

    @GetMapping(value = "/authenticated-info", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "인증된 사용자 정보 조회", description = "JWT를 통해 인증된 사용자 정보를 조회합니다.", operationId = "getAuthenticatedUserInfo", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "인증된 사용자 정보 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RUser.class)))
    public Mono<?> getAuthenticatedUserInfo(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> userAPIService.getActiveUser(user.getEmail())
                        .doOnNext(rUser -> log.debug("required authed user : {}", rUser))
                        .switchIfEmpty(Mono.error(ERROR_USER_NOT_FOUND)));
    }

    @GetMapping(value = "/optional-info", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "옵션 사용자 정보 조회", description = "JWT를 통해 인증된 사용자 정보 또는 기본 정보를 조회합니다.", operationId = "getOptionalUserInfo", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RUser.class)))
    public Mono<?> getOptionalUserInfo(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails) {
        return AuthUtils.optional(userDetails)
                .flatMap(user -> userAPIService.getActiveUser(user.getEmail())
                        .doOnNext(rUser -> log.debug("option authed user : {}", rUser))
                        .switchIfEmpty(Mono.error(ERROR_USER_NOT_FOUND)));
    }
}
