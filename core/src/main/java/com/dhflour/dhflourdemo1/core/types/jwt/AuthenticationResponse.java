package com.dhflour.dhflourdemo1.core.types.jwt;

import com.dhflour.dhflourdemo1.core.domain.user.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class AuthenticationResponse implements Serializable {
    @Schema(description = "JWT Access Token", required = true, example = "JWT")
    private final String accessToken;

    @Setter
    @Schema(description = "User")
    private UserEntity user;

    public AuthenticationResponse(String jwt, UserEntity user) {
        this.accessToken = jwt;
        this.user = user;
    }

}
