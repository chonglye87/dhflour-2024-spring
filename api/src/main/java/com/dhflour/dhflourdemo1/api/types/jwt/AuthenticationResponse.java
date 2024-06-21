package com.dhflour.dhflourdemo1.api.types.jwt;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class AuthenticationResponse implements Serializable {

    @Schema(description = "JWT Access Token", requiredMode = Schema.RequiredMode.REQUIRED, example = "접근 토큰")
    private final String accessToken;

    @Schema(description = "JWT Refresh Token", requiredMode = Schema.RequiredMode.REQUIRED, example = "접근 토큰")
    private final String refreshToken;

    @Setter
    @Schema(description = "로그인 사용자 정보", requiredMode = Schema.RequiredMode.REQUIRED)
    private RUser user;

    public AuthenticationResponse(String accessToken, String refreshToken, RUser user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

}
