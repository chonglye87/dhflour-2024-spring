package com.dhflour.dhflourdemo1.api.types.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class AuthenticationRequest implements Serializable {

    @Schema(description = "email", required = true, example = "test@test.com")
    private String email;

    @Schema(description = "password", required = true, example = "1234")
    private String password;
}
