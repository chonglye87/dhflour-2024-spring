package com.dhflour.dhflourdemo1.api.types.jwt;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class AuthenticationResponse implements Serializable {

    @Schema(description = "JWT Access Token", required = true, example = "접근 토큰")
    private final String accessToken;

    @Setter
    @Schema(description = "로그인 사용자 정보", required = true)
    private RUser user;

    public AuthenticationResponse(String jwt, RUser user) {
        this.accessToken = jwt;
        this.user = user;
    }

}
