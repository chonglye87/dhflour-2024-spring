package com.dhflour.dhflourdemo1.core.types.jwt;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenResponse implements Serializable {
    private String accessToken;
    private String refreshToken;

}
