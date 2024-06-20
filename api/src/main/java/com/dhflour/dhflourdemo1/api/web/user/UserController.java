package com.dhflour.dhflourdemo1.api.web.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.domain.user.RequestSignUp;
import com.dhflour.dhflourdemo1.api.service.user.UserAPIService;
import com.dhflour.dhflourdemo1.api.types.jwt.AuthenticationResponse;
import com.dhflour.dhflourdemo1.core.types.error.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequestMapping(value = "/api/v1/user")
@Tag(name = "회원 API", description = "회원가입 및 회원정보 조회에 대한 API")
@RestController
public class UserController {

    @Autowired
    private UserAPIService userAPIService;

    @Autowired
    private UserDetailsRepositoryReactiveAuthenticationManager authenticationManager;

    @Operation(summary = "[user-1] 회원가입 (Sign Up)",
            description = "회원가입을 합니다.",
            operationId = "signUp")
    @ApiResponse(responseCode = "201", description = "성공적으로 회원가입을 하였음",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthenticationResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AuthenticationResponse>> signUp(@RequestBody Mono<RequestSignUp> monoBody, ServerWebExchange exchange) {

        final AtomicReference<String> email = new AtomicReference<>("");
        final AtomicReference<String> password = new AtomicReference<>("");

        return monoBody
                .filter(request -> StringUtils.isNotEmpty(request.getEmail()))
                .switchIfEmpty(Mono.error(new BadRequestException("Email is empty")))
                .flatMap(request -> userAPIService.exist(request.getEmail())
                        .flatMap(exists -> exists ? Mono.error(new BadRequestException("Email already exists")) : Mono.just(request)))
                .filter(request -> StringUtils.isNotEmpty(request.getUsername()))
                .switchIfEmpty(Mono.error(new BadRequestException("Username is empty")))
                .filter(request -> StringUtils.isNotEmpty(request.getMobile()))
                .switchIfEmpty(Mono.error(new BadRequestException("Mobile is empty")))
                .filter(request -> StringUtils.isNotEmpty(request.getPassword()))
                .switchIfEmpty(Mono.error(new BadRequestException("Password is empty")))
                .flatMap(request -> {
                    email.set(request.getEmail());
                    password.set(request.getPassword());
                    return userAPIService.signUp(request);
                })
                .flatMap(request -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email.get(), password.get())))
                .flatMap(authentication -> userAPIService.authenticate(authentication, exchange))
                .map(authBody -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(authBody))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}
