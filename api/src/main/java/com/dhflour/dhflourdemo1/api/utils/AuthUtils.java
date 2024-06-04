package com.dhflour.dhflourdemo1.api.utils;

import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import com.dhflour.dhflourdemo1.core.types.error.UnauthorizedException;
import reactor.core.publisher.Mono;

public class AuthUtils {
    public static void required(Mono<ReactiveUserDetails> userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("Invalid or missing access token");
        }
    }
}
