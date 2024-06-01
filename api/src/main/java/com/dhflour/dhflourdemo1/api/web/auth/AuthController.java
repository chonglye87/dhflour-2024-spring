package com.dhflour.dhflourdemo1.api.web.auth;

import com.dhflour.dhflourdemo1.core.domain.user.UserEntity;
import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.core.service.user.UserService;
import com.dhflour.dhflourdemo1.core.service.userdetail.MyUserDetailsService;
import com.dhflour.dhflourdemo1.core.types.error.ErrorResponse;
import com.dhflour.dhflourdemo1.core.types.jwt.AuthenticationRequest;
import com.dhflour.dhflourdemo1.core.types.jwt.AuthenticationResponse;
import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RequestMapping(value = "/api/v1/authenticate")
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTSymmetricService jwtService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "[auth-1] JWT 발행",
            description = "JWT 발행합니다.",
            operationId = "authenticate")
    @ApiResponse(responseCode = "200", description = "JWT 발행함",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthenticationResponse.class)))
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (Exception e) {
            // ID / PW
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder().message("ID/PW를 다시 확인해주세요.").build());
        }

        final MyUserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt, userService.get(Locale.KOREA, userDetails.getId())));

    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "인증된 사용자 정보 조회",
            description = "JWT를 통해 인증된 사용자 정보를 조회합니다.",
            operationId = "getAuthenticatedUserInfo",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "인증된 사용자 정보 조회 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserEntity.class)))
    public ResponseEntity<?> getAuthenticatedUserInfo(@AuthenticationPrincipal MyUserDetails userDetails) {
        return ResponseEntity.ok(userService.get(Locale.KOREA, userDetails.getId()));
    }
}
