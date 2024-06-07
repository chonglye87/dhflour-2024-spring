package com.dhflour.dhflourdemo1.core.types.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class RefreshTokenRequest implements Serializable {
    private String refreshToken;
}