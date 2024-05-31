package com.dhflour.dhflourdemo1.core.types.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class AuthenticationResponse implements Serializable {
    @Schema(description = "JWT Access Token", required = true, example = "JWT")
    private final String accessToken;

    public AuthenticationResponse(String jwt) {
        this.accessToken = jwt;
    }

}