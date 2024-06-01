package com.dhflour.dhflourdemo1.api.web.auth;

import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.core.service.user.UserService;
import com.dhflour.dhflourdemo1.core.service.userdetail.MyUserDetailsService;
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
            throw new Exception("Incorrect username or password", e);
        }
        try {
            final MyUserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
            final String jwt = jwtService.generateToken(userDetails);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt);
            authenticationResponse.setUser(userService.get(Locale.KOREA, userDetails.getId()));
            return ResponseEntity.ok(authenticationResponse);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();

    }

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "인증된 사용자 정보 조회",
            description = "JWT를 통해 인증된 사용자 정보를 조회합니다.",
            operationId = "getAuthenticatedUserInfo",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "인증된 사용자 정보 조회 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MyUserDetails.class)))
    public ResponseEntity<Map> getAuthenticatedUserInfo(@AuthenticationPrincipal MyUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", userDetails.getId());
        response.put("email", userDetails.getEmail());
        return ResponseEntity.ok(response);
    }
}
